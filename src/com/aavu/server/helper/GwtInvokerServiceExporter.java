/*
 * Copyright 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.aavu.server.helper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestHandler;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStream;


public class GwtInvokerServiceExporter extends RemoteExporter
        implements HttpRequestHandler, InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CONTENT_TYPE_TEXT_PLAIN_UTF8 = "text/plain; charset=utf-8";
    private static final String GENERIC_FAILURE_MSG = "The call failed on the server; see server log for details";

    private static final HashMap TYPE_NAMES;

    private final ServerSerializableTypeOracle serializableTypeOracle;
    private Object proxy;

    static {
        TYPE_NAMES = new HashMap();
        TYPE_NAMES.put("Z", boolean.class);
        TYPE_NAMES.put("B", byte.class);
        TYPE_NAMES.put("C", char.class);
        TYPE_NAMES.put("D", double.class);
        TYPE_NAMES.put("F", float.class);
        TYPE_NAMES.put("I", int.class);
        TYPE_NAMES.put("J", long.class);
        TYPE_NAMES.put("S", short.class);
    }

    public GwtInvokerServiceExporter() {
        serializableTypeOracle = new ServerSerializableTypeOracleImpl(getPackagePaths());
        
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Assert.notNull(this.proxy, "GwtInvokerServiceExporter has not been initialized");

        Throwable caught;
        try {
            String requestPayload = readPayloadAsUtf8(request);

            String responsePayload = processCall(requestPayload);

            writeResponse(request, response, responsePayload);
            return;
        } catch (IOException e) {
            caught = e;
        } catch (ServletException e) {
            caught = e;
        } catch (SerializationException e) {
            caught = e;
        } catch (Throwable e) {
            caught = e;
        }
        respondWithFailure(response, caught);
    }

    private String processCall(String requestPayload) throws SerializationException {

        ServerSerializationStream stream = new ServerSerializationStream(serializableTypeOracle);

        stream.prepareToRead(requestPayload);

        String serviceIntfName = (String) stream.readObject();

        String methodName = (String) stream.readObject();

        // Read the number and names of the parameter classes from the stream.
        // We have to do this so that we can find the correct overload of the
        // method.
        //
        int paramCount = stream.readInt();
        Class[] paramTypes = new Class[paramCount];
        for (int i = 0; i < paramTypes.length; i++) {
            String paramClassName = (String) stream.readObject();
            try {
                paramTypes[i] = getClassFromName(paramClassName);
            } catch (ClassNotFoundException e) {
                throw new SerializationException("Unknown parameter " + i + " type '"
                        + paramClassName + "'", e);
            }
        }

        Method serviceIntfMethod = findInterfaceMethod(getServiceInterface(), methodName,
                paramTypes, true);

        if (serviceIntfMethod == null) {
            throw new SecurityException("Method '" + methodName
                    + "' (or a particular overload) on interface '" + serviceIntfName
                    + "' was not found, this is either misconfiguration or a hack attempt");
        }

        // Deserialize the parameters.
        //
        Object[] args = new Object[paramCount];
        for (int i = 0; i < args.length; i++) {
            args[i] = stream.deserializeValue(paramTypes[i]);
        }

        // Make the call via reflection.
        //
        String responsePayload = GENERIC_FAILURE_MSG;
        Throwable caught = null;
        try {
            Class returnType = serviceIntfMethod.getReturnType();
            Object returnVal = serviceIntfMethod.invoke(this.proxy, args);
            responsePayload = createResponse(stream, returnType, returnVal, false);
        } catch (IllegalArgumentException e) {
            caught = e;
        } catch (IllegalAccessException e) {
            caught = e;
        } catch (InvocationTargetException e) {
            caught = e;
        }

        if (caught != null) {
            responsePayload = GENERIC_FAILURE_MSG;

            // Be more specific if it's a declared exception.
            //
            if (caught instanceof InvocationTargetException) {
                Throwable cause = ((InvocationTargetException) caught).getCause();
                if (cause != null) {
                    if (isExpectedException(serviceIntfMethod, cause)) {
                        Class thrownClass = cause.getClass();
                        responsePayload = createResponse(stream, thrownClass, cause, true);
                    }
                }
            }
        }

        return responsePayload;
    }


    private String readPayloadAsUtf8(HttpServletRequest request)
            throws IOException, ServletException {

        int contentLength = request.getContentLength();
        if (contentLength == -1) {
            // Content length must be known.
            throw new ServletException("Content-Length must be specified");
        }

        String contentType = request.getContentType();
        boolean contentTypeIsOkay = false;
        // Content-Type must be specified.
        if (contentType != null) {
            // The type must be plain text.
            if (contentType.startsWith("text/plain")) {
                // And it must be UTF-8 encoded (or unspecified, in which case we assume
                // that it's either UTF-8 or ASCII).
                if (contentType.indexOf("charset=") == -1) {
                    contentTypeIsOkay = true;
                } else if (contentType.indexOf("charset=utf-8") != -1) {
                    contentTypeIsOkay = true;
                }
            }
        }
        if (!contentTypeIsOkay) {
            throw new ServletException("Content-Type must be 'text/plain' with 'charset=utf-8'" +
                    " (or unspecified charset)");
        }

        InputStream in = request.getInputStream();
        try {
            byte[] payload = new byte[contentLength];
            int offset = 0;
            int len = contentLength;
            int byteCount;
            while (offset < contentLength) {
                byteCount = in.read(payload, offset, len);
                if (byteCount == -1) {
                    throw new ServletException("Client did not send " + contentLength
                            + " bytes as expected");
                }
                offset += byteCount;
                len -= byteCount;
            }
            return new String(payload, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private Class getClassFromName(String name) throws ClassNotFoundException {
        Object value = TYPE_NAMES.get(name);
        if (value != null) {
            return (Class) value;
        }
        return Class.forName(name, false, this.getClass().getClassLoader());
    }

    private String createResponse(ServerSerializationStream stream
            , Class responseType, Object responseObj, boolean isException) {
        stream.prepareToWrite();
        stream.writeInt(SerializationStream.SERIALIZATION_STREAM_VERSION);
        stream.writeInt(stream.getFlags());
        if (responseType != void.class) {
            try {
                stream.serializeValue(responseObj, responseType);
            } catch (SerializationException e) {
                isException = true;
            }
        }
        return (isException ? "{EX}" : "{OK}") + stream.toString();
    }

    /**
     * Find the invoked method on either the specified interface or any super.
     */
    private static Method findInterfaceMethod(Class intf, String methodName,
                                              Class[] paramTypes, boolean includeInherited) {
        try {
            return intf.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            if (includeInherited) {
                Class[] superintfs = intf.getInterfaces();
                for (int i = 0; i < superintfs.length; i++) {
                    Method method = findInterfaceMethod(superintfs[i], methodName,
                            paramTypes, true);
                    if (method != null)
                        return method;
                }
            }

            return null;
        }
    }

    private boolean isExpectedException(Method serviceIntfMethod, Throwable cause) {
        assert (serviceIntfMethod != null);
        assert (cause != null);

        Class[] exceptionsThrown = serviceIntfMethod.getExceptionTypes();
        if (exceptionsThrown.length <= 0) {
            return false;
        }

        Class causeType = cause.getClass();

        for (int index = 0; index < exceptionsThrown.length; ++index) {
            Class exceptionThrown = exceptionsThrown[index];
            assert (exceptionThrown != null);

            if (exceptionThrown.isAssignableFrom(causeType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the machinery of this class itself has a problem, rather than
     * the invoked third-party method. It writes a simple 500 message back to the
     * client.
     */
    private void respondWithFailure(HttpServletResponse response, Throwable caught) {
        logger.error("Exception while dispatching incoming RPC call", caught);
        try {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String msg = "The call failed on the server; see server log for details";
            response.getWriter().write(msg);
        } catch (IOException e) {
            logger.error("sendError() failed while sending the previous failure to the client",
                    caught);
        }
    }

    private void writeResponse(HttpServletRequest request,
                               HttpServletResponse response, String responsePayload) throws IOException {

        byte[] reply = responsePayload.getBytes(CHARSET_UTF8);
        String contentType = CONTENT_TYPE_TEXT_PLAIN_UTF8;

        // Send the reply.
        //
        response.setContentLength(reply.length);
        response.setContentType(contentType);
        response.getOutputStream().write(reply);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Obtain the special package-prefixes we use to check for custom serializers
     * that would like to live in a package that they cannot. For example,
     * "java.util.ArrayList" is in a sealed package, so instead we use this prefix
     * to check for a custom serializer in
     * "com.google.gwt.user.client.rpc.core.java.util.ArrayList". Right now, it's
     * hard-coded because we don't have a pressing need for this mechanism to be
     * extensible, but it is imaginable, which is why it's implemented this way.
     */
    private String[] getPackagePaths() {
        return new String[]{"com.google.gwt.user.client.rpc.core"};
    }

    public void afterPropertiesSet() {
        this.proxy = getProxyForService();
    }
}
