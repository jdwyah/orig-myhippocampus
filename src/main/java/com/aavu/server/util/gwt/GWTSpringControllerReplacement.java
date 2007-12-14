package com.aavu.server.util.gwt;

/*
 * Copyright 2006 George Georgovassilis <g.georgovassilis[at]gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Simple spring controller that merges GWT's {@link RemoteServiceServlet},
 * the {@link Controller} and also implements the {@link RemoteService}
 * interface so as to be able to directly delegate RPC calls to extending
 * classes.
 * 
 * @author g.georgovassilis
 * 
 */

public class GWTSpringControllerReplacement extends RemoteServiceServlet
        implements ServletContextAware, Controller, RemoteService {

    private static final Logger log = Logger
            .getLogger(GWTSpringControllerReplacement.class);

    private static final long serialVersionUID = 5399966488983189122L;

    private boolean serializeEverything = false;

    public void setSerializeEverything(boolean serializeEverything) {
        this.serializeEverything = serializeEverything;
    }

    @Override
    public String processCall(String payload)
            throws SerializationException {
        try {

            RPCRequest rpcRequest = RPC.decodeRequest(payload, this
                    .getClass(), this);

            // RPC.invokeAndEncodeResponse(this,
            // rpcRequest.getMethod(), rpcRequest.getParameters(),
            // rpcRequest.getSerializationPolicy());

            return RPCWithHibernateSupport1529.invokeAndEncodeResponse(
                    this, rpcRequest.getMethod(), rpcRequest
                            .getParameters(), rpcRequest
                            .getSerializationPolicy());

        } catch (IncompatibleRemoteServiceException ex) {
            getServletContext()
                    .log(
                            "An IncompatibleRemoteServiceException was thrown while processing this call.",
                            ex);
            return RPC.encodeResponseForFailure(null, ex);
        }
    }

    /**
     * We can use our funky laissez faire 1.4.10 (RC1) style serialization
     * policy to serialize everything which means we don't need to
     * recompile all the gwt stuff just to restart jetty.
     */
    @Override
    protected SerializationPolicy doGetSerializationPolicy(
            HttpServletRequest request, String moduleBaseURL,
            String strongName) {
        if (serializeEverything) {
            log.warn("Using 1.4.10 (RC1) style serializaion.");
            return OneFourTenSerializationPolicy.getInstance();
        } else {
            log.debug("Using Standard Serialization.");
            return super.doGetSerializationPolicy(request, moduleBaseURL,
                    strongName);
        }
    }

    private static ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> servletResponse = new ThreadLocal<HttpServletResponse>();

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Return the request which invokes the service. Valid only if used in
     * the dispatching thread.
     * 
     * @return the servlet request
     */
    public static HttpServletRequest getRequest() {
        return servletRequest.get();
    }

    /**
     * Return the response which accompanies the request. Valid only if
     * used in the dispatching thread.
     * 
     * @return the servlet response
     */
    public static HttpServletResponse getResponse() {
        return servletResponse.get();
    }

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            servletRequest.set(request);
            servletResponse.set(response);
            doPost(request, response);
        } finally {
            servletRequest.set(null);
            servletResponse.set(null);
        }
        return null;
    }
}
