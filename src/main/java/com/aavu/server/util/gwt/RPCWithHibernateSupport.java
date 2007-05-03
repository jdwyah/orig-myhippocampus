package com.aavu.server.util.gwt;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracle;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracleImpl;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

/**
 * Utility class for integrating with the RPC system. This class exposes methods
 * for decoding of RPC requests, encoding of RPC responses, and invocation of
 * RPC calls on service objects. The operations exposed by this class can be
 * reused by framework implementors such as Spring and G4jsf to support a wide
 * range of service invocation policies.
 * 
 * <h3>Canonical Example</h3>
 * The following example demonstrates the canonical way to use this class.
 * 
 * {@example com.google.gwt.examples.rpc.server.CanonicalExample#processCall(String)}
 * 
 * <h3>Advanced Example</h3>
 * The following example shows a more advanced way of using this class to create
 * an adapter between GWT RPC entities and POJOs.
 * 
 * {@example com.google.gwt.examples.rpc.server.AdvancedExample#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 */
public final class RPCWithHibernateSupport {

  /**
   * Maps primitive wrapper classes to their corresponding primitive class.
   */
  private static final Map PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS = new HashMap();

  /**
   * Oracle used in stream construction. Encapsulates a set of static,
   * synchronized caches.
   */
  private static ServerSerializableTypeOracle serializableTypeOracle;

  /**
   * Static map of classes to sets of interfaces (e.g. classes). Optimizes
   * lookup of interfaces for security.
   */
  private static Map/* <Class, Set<String> > */serviceToImplementedInterfacesMap;

  private static final HashMap TYPE_NAMES;
  static {
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Boolean.class, Boolean.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Byte.class, Byte.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Character.class,
        Character.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Double.class, Double.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Float.class, Float.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Integer.class, Integer.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Long.class, Long.TYPE);
    PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Short.class, Short.TYPE);

    TYPE_NAMES = new HashMap();
    TYPE_NAMES.put("Z", boolean.class);
    TYPE_NAMES.put("B", byte.class);
    TYPE_NAMES.put("C", char.class);
    TYPE_NAMES.put("D", double.class);
    TYPE_NAMES.put("F", float.class);
    TYPE_NAMES.put("I", int.class);
    TYPE_NAMES.put("J", long.class);
    TYPE_NAMES.put("S", short.class);

    serializableTypeOracle = new ServerSerializableTypeOracleImpl(
        getPackagePaths());

    serviceToImplementedInterfacesMap = new HashMap();
  }

  /**
   * Returns an {@link RPCRequest} that is built by decoding the contents of an
   * encoded RPC request.
   * 
   * <p>
   * This method is equivalent to calling {@link #decodeRequest(String, Class)}
   * with <code>null</code> for the type parameter.
   * </p>
   * 
   * @param encodedRequest a string that encodes the {@link RemoteService}
   *          interface, the service method to call, and the arguments to for
   *          the service method
   * @return an {@link RPCRequest} instance
   * 
   * @throws SerializationException if the encodedRequest contents cannot be
   *           deserialized
   * @throws SecurityException if any of the following conditions apply:
   *           <ul>
   *           <li><code>RPC.class.getClassLoader()</code> cannot load the
   *           service interface requested in the encoded request</li>
   *           <li>the requested interface is not assignable to
   *           {@link RemoteService}</li>
   *           <li>the service method requested in the encoded request is not a
   *           member of the requested service interface</li>
   *           </ul>
   */
  public static RPCRequest decodeRequest(String encodedRequest)
      throws SerializationException {
    return decodeRequest(encodedRequest, null);
  }

  /**
   * Returns an {@link RPCRequest} that is built by decoding the contents of an
   * encoded RPC request and optionally validating that type can handle the
   * request. If the type parameter is not <code>null</code>, the
   * implementation checks that the type is assignable to the
   * {@link RemoteService} interface requested in the encoded request string.
   * 
   * <p>
   * Invoking this method with <code>null</code> for the type parameter,
   * <code>decodeRequest(encodedRequest, null)</code>, is equivalent to
   * calling <code>decodeRequest(encodedRequest)</code>.
   * </p>
   * 
   * @param encodedRequest a string that encodes the {@link RemoteService}
   *          interface, the service method, and the arguments to pass to the
   *          service method
   * @param type if not <code>null</code>, the implementation checks that the
   *          type is assignable to the {@link RemoteService} interface encoded
   *          in the encoded request string.
   * @return an {@link RPCRequest} instance
   * 
   * @throws NullPointerException if the encodedRequest is <code>null</code>
   * @throws IllegalArgumentException if the encodedRequest is an empty string
   * @throws SerializationException if the types in the encoded request cannot
   *           be deserialized
   * @throws SecurityException if any of the following conditions apply:
   *           <ul>
   *           <li><code>RPC.class.getClassLoader()</code> cannot load the
   *           service interface requested in the encodedRequest</li>
   *           <li>the requested interface is not assignable to
   *           {@link RemoteService}</li>
   *           <li>the service method requested in the encodedRequest is not a
   *           member of the requested service interface</li>
   *           <li>the type parameter is not <code>null</code> and is not
   *           assignable to the requested {@link RemoteService} interface
   *           </ul>
   */
  public static RPCRequest decodeRequest(String encodedRequest, Class type)
      throws SerializationException {
    if (encodedRequest == null) {
      throw new NullPointerException("encodedRequest cannot be null");
    }

    if (encodedRequest.length() == 0) {
      throw new IllegalArgumentException("encodedRequest cannot be empty");
    }

    ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(
        serializableTypeOracle);
    streamReader.prepareToRead(encodedRequest);

    String serviceIntfName = streamReader.readString();

    if (type != null) {
      if (!implementsInterface(type, serviceIntfName)) {
        // The service does not implement the requested interface
        throw new SecurityException("Blocked attempt to access interface '"
            + serviceIntfName + "', which is not implemented by '"
            + printTypeName(type)
            + "'; this is either misconfiguration or a hack attempt");
      }
    }

    Class serviceIntf;
    try {
      serviceIntf = getClassFromSerializedName(serviceIntfName);
      if (!RemoteService.class.isAssignableFrom(serviceIntf)) {
        // The requested interface is not a RemoteService interface
        throw new SecurityException(
            "Blocked attempt to access interface '"
                + printTypeName(serviceIntf)
                + "', which doesn't extend RemoteService; this is either misconfiguration or a hack attempt");
      }
    } catch (ClassNotFoundException e) {
      SecurityException securityException = new SecurityException(
          "Could not locate requested interface '" + serviceIntfName
              + "' in default classloader");
      securityException.initCause(e);
      throw securityException;
    }

    String serviceMethodName = streamReader.readString();

    int paramCount = streamReader.readInt();
    Class[] parameterTypes = new Class[paramCount];

    for (int i = 0; i < parameterTypes.length; i++) {
      String paramClassName = streamReader.readString();
      try {
        parameterTypes[i] = getClassFromSerializedName(paramClassName);
      } catch (ClassNotFoundException e) {
        throw new SerializationException("Unknown parameter " + i + " type '"
            + paramClassName + "'", e);
      }
    }

    Method method = findInterfaceMethod(serviceIntf, serviceMethodName,
        parameterTypes, true);

    if (method == null) {
      throw new SecurityException(formatMethodNotFoundErrorMessage(serviceIntf,
          serviceMethodName, parameterTypes));
    }

    Object[] parameterValues = new Object[parameterTypes.length];
    for (int i = 0; i < parameterValues.length; i++) {
      parameterValues[i] = streamReader.deserializeValue(parameterTypes[i]);
    }

    return new RPCRequest(method, parameterValues);
  }

  /**
   * Returns a string that encodes an exception. It is an error to try to encode
   * an exception that is not in the method's list of checked exceptions.
   * 
   * @param serviceMethod the method that threw the exception
   * @param cause the {@link Throwable} that was thrown
   * @return a string that encodes the exception, if the exception was expected
   * 
   * @throws NullPointerException if either the serviceMethod or the cause are
   *           <code>null</code>
   * @throws SerializationException if the result cannot be serialized
   * @throws UnexpectedException if the result was an unexpected exception (a
   *           checked exception not declared in the serviceMethod's signature)
   */
  public static String encodeResponseForFailure(Method serviceMethod,
      Throwable cause) throws SerializationException {
    if (serviceMethod == null) {
      throw new NullPointerException("serviceMethod cannot be null");
    }

    if (cause == null) {
      throw new NullPointerException("cause cannot be null");
    }

    if (isExpectedException(serviceMethod, cause)) {
      return encodeResponse(cause.getClass(), cause, true);
    } else {
      throw new UnexpectedException("Service method '"
          + getSourceRepresentation(serviceMethod)
          + "' threw an unexpected exception: " + cause.toString(), cause);
    }
  }

  /**
   * Returns a string that encodes the object. It is an error to try to encode
   * an object that is not assignable to the service method's return type.
   * 
   * @param serviceMethod the method whose result we are encoding
   * @param object the instance that we wish to encode
   * @return a string that encodes the object, if the object is compatible with
   *         the service method's declared return type
   * 
   * @throws IllegalArgumentException if the result is not assignable to the
   *           service method's return type
   * @throws NullPointerException if the service method is <code>null</code>
   * @throws SerializationException if the result cannot be serialized
   */
  public static String encodeResponseForSuccess(Method serviceMethod,
      Object object) throws SerializationException {
    if (serviceMethod == null) {
      throw new NullPointerException("serviceMethod cannot be null");
    }

    Class methodReturnType = serviceMethod.getReturnType();
    if (methodReturnType != void.class && object != null) {
      Class actualReturnType;
      if (methodReturnType.isPrimitive()) {
        actualReturnType = getPrimitiveClassFromWrapper(object.getClass());
      } else {
        actualReturnType = object.getClass();
      }

      if (actualReturnType == null
          || !methodReturnType.isAssignableFrom(actualReturnType)) {
        throw new IllegalArgumentException("Type '"
            + printTypeName(object.getClass())
            + "' does not match the return type in the method's signature: '"
            + getSourceRepresentation(serviceMethod) + "'");
      }
    }

    return encodeResponse(methodReturnType, object, false);
  }

  /**
   * Returns a string that encodes the result of calling a service method, which
   * could be the value returned by the method or an exception thrown by it.
   * 
   * <p>
   * This method does no security checking; security checking must be done on
   * the method prior to this invocation.
   * </p>
   * 
   * @param target instance on which to invoke the serviceMethod
   * @param serviceMethod the method to invoke
   * @param args arguments used for the method invocation
   * @return a string which encodes either the method's return or a checked
   *         exception thrown by the method
   * 
   * @throws SecurityException if the method cannot be accessed or if the number
   *           or type of actual and formal arguments differ
   * @throws SerializationException if an object could not be serialized by the
   *           stream
   * @throws UnexpectedException if the serviceMethod throws a checked exception
   *           that is not declared in its signature
   */
  public static String invokeAndEncodeResponse(Object target,
      Method serviceMethod, Object[] args) throws SerializationException {

    if (serviceMethod == null) {
      throw new NullPointerException("serviceMethod cannot be null");
    }

    String responsePayload;
    try {
      Object result = serviceMethod.invoke(target, args);

      responsePayload = encodeResponseForSuccess(serviceMethod, result);
    } catch (IllegalAccessException e) {
      SecurityException securityException = new SecurityException(
          formatIllegalAccessErrorMessage(target, serviceMethod));
      securityException.initCause(e);
      throw securityException;
    } catch (IllegalArgumentException e) {
      SecurityException securityException = new SecurityException(
          formatIllegalArgumentErrorMessage(target, serviceMethod, args));
      securityException.initCause(e);
      throw securityException;
    } catch (InvocationTargetException e) {
      // Try to encode the caught exception
      //
      Throwable cause = e.getCause();

      responsePayload = encodeResponseForFailure(serviceMethod, cause);
    }

    return responsePayload;
  }

  /**
   * Returns a string that encodes the results of an RPC call. Private overload
   * that takes a flag signaling the preamble of the response payload.
   * 
   * @param serviceMethod the method whose return object we are encoding
   * @param object the object that we wish to send back to the client
   * @param wasThrown if true, the object being returned was an exception thrown
   *          by the service method; if false, it was the result of the service
   *          method's invocation
   * @return a string that encodes the response from a service method
   * @throws SerializationException if the object cannot be serialized
   */
  private static String encodeResponse(Class responseClass, Object object,
      boolean wasThrown) throws SerializationException {

	  ServerSerializationStreamWriterWithHibernateEscaping stream = new ServerSerializationStreamWriterWithHibernateEscaping(
        serializableTypeOracle);

    stream.prepareToWrite();
    if (responseClass != void.class) {
      stream.serializeValue(object, responseClass);
    }

    String bufferStr = (wasThrown ? "//EX" : "//OK") + stream.toString();
    return bufferStr;
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
          if (method != null) {
            return method;
          }
        }
      }

      return null;
    }
  }

  private static String formatIllegalAccessErrorMessage(Object target,
      Method serviceMethod) {
    StringBuffer sb = new StringBuffer();
    sb.append("Blocked attempt to access inaccessible method '");
    sb.append(getSourceRepresentation(serviceMethod));
    sb.append("'");

    if (target != null) {
      sb.append(" on target '");
      sb.append(printTypeName(target.getClass()));
      sb.append("'");
    }

    sb.append("; this is either misconfiguration or a hack attempt");

    return sb.toString();
  }

  private static String formatIllegalArgumentErrorMessage(Object target,
      Method serviceMethod, Object[] args) {
    StringBuffer sb = new StringBuffer();
    sb.append("Blocked attempt to invoke method '");
    sb.append(getSourceRepresentation(serviceMethod));
    sb.append("'");

    if (target != null) {
      sb.append(" on target '");
      sb.append(printTypeName(target.getClass()));
      sb.append("'");
    }

    sb.append(" with invalid arguments");

    if (args != null && args.length > 0) {
      sb.append(Arrays.asList(args));
    }

    return sb.toString();
  }

  private static String formatMethodNotFoundErrorMessage(Class serviceIntf,
      String serviceMethodName, Class[] parameterTypes) {
    StringBuffer sb = new StringBuffer();

    sb.append("Could not locate requested method '");
    sb.append(serviceMethodName);
    sb.append("(");
    for (int i = 0; i < parameterTypes.length; ++i) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(printTypeName(parameterTypes[i]));
    }
    sb.append(")'");

    sb.append(" in interface '");
    sb.append(printTypeName(serviceIntf));
    sb.append("'");

    return sb.toString();
  }

  /**
   * Returns the {@link Class} instance for the named class or primitive type.
   * 
   * @param serializedName the serialized name of a class or primitive type
   * @return Class instance for the given type name
   * @throws ClassNotFoundException if the named type was not found
   */
  private static Class getClassFromSerializedName(String serializedName)
      throws ClassNotFoundException {
    Object value = TYPE_NAMES.get(serializedName);
    if (value != null) {
      return (Class) value;
    }

    return Class.forName(serializedName, false, RPC.class.getClassLoader());
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
  private static String[] getPackagePaths() {
    return new String[] {"com.google.gwt.user.client.rpc.core"};
  }

  /**
   * Returns the {@link java.lang.Class Class} for a primitive type given its
   * corresponding wrapper {@link java.lang.Class Class}.
   * 
   * @param wrapperClass primitive wrapper class
   * @return primitive class
   */
  private static Class getPrimitiveClassFromWrapper(Class wrapperClass) {
    return (Class) PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.get(wrapperClass);
  }

  /**
   * Returns the source representation for a method signature.
   * 
   * @param method method to get the source signature for
   * @return source representation for a method signature
   */
  private static String getSourceRepresentation(Method method) {
    return method.toString().replace('$', '.');
  }

  /**
   * Used to determine whether the specified interface name is implemented by
   * the service class. This is done without loading the class (for security).
   */
  private static boolean implementsInterface(Class service, String intfName) {
    synchronized (serviceToImplementedInterfacesMap) {
      // See if it's cached.
      //
      Set/* <String> */interfaceSet = (Set) serviceToImplementedInterfacesMap.get(service);
      if (interfaceSet != null) {
        if (interfaceSet.contains(intfName)) {
          return true;
        }
      } else {
        interfaceSet = new HashSet/* <String> */();
        serviceToImplementedInterfacesMap.put(service, interfaceSet);
      }

      if (!service.isInterface()) {
        while ((service != null) && !RemoteServiceServlet.class.equals(service)) {
          Class[] intfs = service.getInterfaces();
          for (int i = 0; i < intfs.length; i++) {
            Class intf = intfs[i];
            if (implementsInterfaceRecursive(intf, intfName)) {
              interfaceSet.add(intfName);
              return true;
            }
          }

          // did not find the interface in this class so we look in the
          // superclass
          //
          service = service.getSuperclass();
        }
      } else {
        if (implementsInterfaceRecursive(service, intfName)) {
          interfaceSet.add(intfName);
          return true;
        }
      }

      return false;
    }
  }

  /**
   * Only called from implementsInterface().
   */
  private static boolean implementsInterfaceRecursive(Class clazz,
      String intfName) {
    assert (clazz.isInterface());

    if (clazz.getName().equals(intfName)) {
      return true;
    }

    // search implemented interfaces
    Class[] intfs = clazz.getInterfaces();
    for (int i = 0; i < intfs.length; i++) {
      Class intf = intfs[i];
      if (implementsInterfaceRecursive(intf, intfName)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if the {@link java.lang.reflect.Method Method} definition on
   * the service is specified to throw the exception contained in the
   * InvocationTargetException or false otherwise. NOTE we do not check that the
   * type is serializable here. We assume that it must be otherwise the
   * application would never have been allowed to run.
   * 
   * @param serviceIntfMethod the method from the RPC request
   * @param cause the exception that the method threw
   * @return true if the exception's type is in the method's signature
   */
  private static boolean isExpectedException(Method serviceIntfMethod,
      Throwable cause) {
    assert (serviceIntfMethod != null);
    assert (cause != null);

    Class[] exceptionsThrown = serviceIntfMethod.getExceptionTypes();
    if (exceptionsThrown.length <= 0) {
      // The method is not specified to throw any exceptions
      //
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
   * Straight copy from
   * {@link com.google.gwt.dev.util.TypeInfo#getSourceRepresentation(Class)} to
   * avoid runtime dependency on gwt-dev.
   */
  private static String printTypeName(Class type) {
    // Primitives
    //
    if (type.equals(Integer.TYPE)) {
      return "int";
    } else if (type.equals(Long.TYPE)) {
      return "long";
    } else if (type.equals(Short.TYPE)) {
      return "short";
    } else if (type.equals(Byte.TYPE)) {
      return "byte";
    } else if (type.equals(Character.TYPE)) {
      return "char";
    } else if (type.equals(Boolean.TYPE)) {
      return "boolean";
    } else if (type.equals(Float.TYPE)) {
      return "float";
    } else if (type.equals(Double.TYPE)) {
      return "double";
    }

    // Arrays
    //
    if (type.isArray()) {
      Class componentType = type.getComponentType();
      return printTypeName(componentType) + "[]";
    }

    // Everything else
    //
    return type.getName().replace('$', '.');
  }

  /**
   * Static classes have no constructability.
   */
  private RPCWithHibernateSupport() {
  }
}
