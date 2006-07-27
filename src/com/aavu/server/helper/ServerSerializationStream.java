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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStream;
import com.google.gwt.user.client.rpc.SerializationStreamObjectDecoder;
import com.google.gwt.user.client.rpc.SerializationStreamObjectEncoder;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.StringTable;
import com.google.gwt.user.rebind.rpc.SerializedInstanceReference;

/**
 * For internal use only. Used for server call serialization. This class is
 * carefully matched with the client-side version.
 *
 * @skip
 */
public class ServerSerializationStream extends SerializationStream {
  private String nextToken;
  private String[] fStringTable;
  private HashMap fStringsSeen = new HashMap();
  private ArrayList fTokenList = new ArrayList();
  private StringBuffer fToStringBuffer = new StringBuffer();
  private int fCurrentIndex;
  private int fTotalTokenCharsWritten;
  private IdentityHashMap fAlreadyEncodedObjects = new IdentityHashMap();
  private ArrayList fAlreadyDecodedObjects = new ArrayList();
  private ServerSerializableTypeOracle serializableTypeOracle;

  public ServerSerializationStream(
      ServerSerializableTypeOracle serializableTypeOracle) {
    this.serializableTypeOracle = serializableTypeOracle;
  }

  protected String getTypeName(String token) {
    assert (token.startsWith(NEW_INSTANCE_MARKER));
    Integer index = Integer.valueOf(token.substring(1));
    return (String) fStringsSeen.get(index);
  }

  public int addString(String string) {
    Object o = fStringsSeen.get(string);
    int index;
    if (o != null) {
      index = ((Integer) o).intValue();
    } else {
      index = fStringsSeen.size();
      fStringsSeen.put(string, new Integer(index));
    }

    return index;
  }

  /**
   * This defines the character used to enclose JavaScript strings.
   */
  private static final char fJSStringEnclosingChar = '\"';

  /**
   * This defines the character used by JavaScript to mark the start of an
   * escape sequence.
   */
  private static final char fJSEscapeChar = '\\';

  public void prepareToRead(String encodedTokens) {
    fAlreadyEncodedObjects.clear();
    fAlreadyDecodedObjects.clear();
    fTokenList.clear();
    clearStringTable();
    fCurrentIndex = 0;

    int idx = 0, nextIdx;
    while (-1 != (nextIdx = encodedTokens.indexOf('\uffff', idx))) {
      String current = encodedTokens.substring(idx, nextIdx);
      fTokenList.add(current);
      idx = nextIdx + 1;
    }

    // Read the stream version
    //
    setVersion(Integer.parseInt(extract()));

    // Read the stream flags
    //
    setFlags(Integer.parseInt(extract()));

    // Read the type name table
    //
    deserializeTypeNameTable();
  }

  private void clearStringTable() {
    fStringsSeen.clear();
    fStringTable = null;
  }

  private void deserializeTypeNameTable() {
    int typeNameCount = readInt();
    fStringTable = new String[typeNameCount];
    for (int typeNameIndex = 0; typeNameIndex < typeNameCount; ++typeNameIndex) {
      fStringTable[typeNameIndex] = extract();
    }
  }

  public String extract() {
    return (String) fTokenList.get(fCurrentIndex++);
  }

  public void prepareToWrite() {
    fAlreadyEncodedObjects.clear();
    fAlreadyDecodedObjects.clear();
    fTokenList.clear();
    fTotalTokenCharsWritten = 0;
    clearStringTable();
  }

  public void append(String token) {
    // The string is either null or prefixed with a '$' (to differentiate b/w
    // null and "")
    //
    fTokenList.add(token);
    if (token != null) {
      fTotalTokenCharsWritten += token.length();
    }
  }

  /**
   * Build an array of JavaScript string literals that can be decoded by the
   * client via the eval function.
   *
   * NOTE: We build the array in reverse so the client can simply use the pop
   * function to remove the next item from the list.
   */
  public String toString() {

    // Build a JavaScript string (with escaping, of course).
    // We take a guess at how big to make to buffer to avoid numerous resizes.
    // The '2' below is from the double quotes around the string literals.
    //
    fToStringBuffer.setLength(0);
    int capacityGuess = 2 * fTotalTokenCharsWritten + 2 * fTokenList.size();
    fToStringBuffer.ensureCapacity(capacityGuess);

    fToStringBuffer.append("[");

    writePayload();

    writeStringTable();

    writeHeader();

    fToStringBuffer.append("]");

    return fToStringBuffer.toString();
  }

  /**
   * Notice that the field are written in reverse order that the client can just
   * pop items out of the stream.
   */
  private void writeHeader() {
    fToStringBuffer.append(",");
    appendToken(fToStringBuffer, Integer.toString(getFlags()));
    fToStringBuffer.append(",");
    appendToken(fToStringBuffer, Integer
      .toString(SerializationStream.SERIALIZATION_STREAM_VERSION));
  }

  /**
   *
   */
  private void writePayload() {
    for (int i = fTokenList.size() - 1; i >= 0; --i) {
      String token = (String) fTokenList.get(i);

      appendToken(fToStringBuffer, token);

      if (i > 0)
        fToStringBuffer.append(",");
    }
  }

  private static void appendToken(StringBuffer sb, String token) {
    if (token != null) {
      sb.append(fJSStringEnclosingChar);
      escape(sb, token);
      sb.append(fJSStringEnclosingChar);
    } else {
      sb.append("null");
    }
  }

  private void writeStringTable() {
    if (fTokenList.size() > 0) {
      fToStringBuffer.append(",");
    }

    // Initialize the type name list
    fStringTable = new String[fStringsSeen.size()];
    Set entrySet = fStringsSeen.entrySet();
    Iterator iter = entrySet.iterator();
    while (iter.hasNext()) {
      Entry entry = (Entry) iter.next();
      fStringTable[((Integer) entry.getValue()).intValue()] = (String) entry
        .getKey();
    }

    fToStringBuffer.append("[");
    for (int index = 0; index < fStringTable.length; ++index) {
      if (index > 0)
        fToStringBuffer.append(",");

      appendToken(fToStringBuffer, fStringTable[index]);
    }
    fToStringBuffer.append("]");
  }

  /**
   * This method takes a string containing characters that need to be escaped
   * and maps them onto their escape sequence. A character will need to be
   * escaped if it is a special JavaScript escape or quoting character.
   *
   * Assumptions: We are targetting a version of JavaScript that that is later
   * than 1.3 that supports unicode strings. Therefore there is no need to
   * escape unicode characters.
   *
   * TODO: Could reduce dup code in the switch statement via an array lookup
   */
  private static void escape(StringBuffer toReceive, String toEscape) {
    char[] chars = toEscape.toCharArray();
    char c;
    for (int i = 0, n = chars.length; i < n; ++i) {
      c = chars[i];
      switch (c) {
        /* JavaScript NUL character '\0' */
        case '\u0000':
          toReceive.append(fJSEscapeChar);
          toReceive.append("0");
          break;
        case '\b':
          toReceive.append(fJSEscapeChar);
          toReceive.append("b");
          break;
        case '\t':
          toReceive.append(fJSEscapeChar);
          toReceive.append("t");
          break;
        case '\n':
          toReceive.append(fJSEscapeChar);
          toReceive.append("n");
          break;
        /* JavaScript Vertical Tab character '\v' */
        case '\u000b':
          toReceive.append(fJSEscapeChar);
          toReceive.append("v");
          break;
        case '\f':
          toReceive.append(fJSEscapeChar);
          toReceive.append("f");
          break;
        case '\r':
          toReceive.append(fJSEscapeChar);
          toReceive.append("r");
          break;
        case fJSEscapeChar:
          toReceive.append("\\\\");
          break;
        case fJSStringEnclosingChar:
          toReceive.append(fJSEscapeChar);
          toReceive.append(fJSStringEnclosingChar);
          break;
        default:
          toReceive.append(c);
      }
    }
  }

  // Called during encode to encode a backref if the object has been seen
  // before.
  // If the caller needs to encode the object, returns true.
  // If o is non-null, never returns true twice.
  public boolean startInstance(SerializationStreamWriter streamWriter,
                               Object instance) throws SerializationException {
    if (instance != null) {
      int index;
      String encodedInstRef;
      if (shouldEnforceTypeVersioning()) {
        encodedInstRef = serializableTypeOracle
          .encodeSerializedInstanceReference(instance.getClass());
      } else {
        encodedInstRef = serializableTypeOracle.getSerializedTypeName(instance
          .getClass());
      }

      index = addString(encodedInstRef);

      Integer idx = (Integer) fAlreadyEncodedObjects.get(instance);
      if (idx != null) {
        append(PREV_INSTANCE_MARKER + idx.intValue());
        return false;
      }

      append(NEW_INSTANCE_MARKER + Integer.toString(index));

      fAlreadyEncodedObjects.put(instance, new Integer(fAlreadyEncodedObjects
        .size()));
      return true;
    }

    append(NULL_INSTANCE_MARKER);
    return false;
  }

  private String getSerializationSignature(Object instance) {
    // TODO(mmendez): make this real
    return "0";
  }

  public void endInstance(SerializationStreamWriter streamWriter,
                          Object instance) {
  }

  public String nextInstance(SerializationStreamReader stream) {
    String current = nextToken;
    nextToken = null;
    if (current.equals("~")) {
      return null;
    }

    return getInstanceName(current);
  }

  public Object prevInstance(SerializationStreamReader stream) {
    nextToken = extract();
    if (nextToken.startsWith(PREV_INSTANCE_MARKER)) {
      return lookupDecodedObject(nextToken);
    }

    return null;
  }

  // Called during decode to allow an object to be coalesced
  public void rememberDecodedObject(Object o) {
    fAlreadyDecodedObjects.add(o);
  }

  // Called during decode to realize the object from a backref
  public Object lookupDecodedObject(String id) {
    return fAlreadyDecodedObjects.get(Integer.parseInt(id.substring(1)));
  }

  public SerializationStreamObjectDecoder getObjectDecoder() {
    return this;
  }

  public SerializationStreamObjectEncoder getObjectEncoder() {
    return this;
  }

  public String getString(int index) {
    assert (index < fStringTable.length);
    return fStringTable[index];
  }

  public Object deserializeValue(Class type) throws SerializationException {
    if (type == boolean.class) {
      return Boolean.valueOf(readBoolean());
    } else if (type == byte.class) {
      return new Byte(readByte());
    } else if (type == char.class) {
      return new Character(readChar());
    } else if (type == double.class) {
      return new Double(readDouble());
    } else if (type == float.class) {
      return new Float(readFloat());
    } else if (type == int.class) {
      return new Integer(readInt());
    } else if (type == long.class) {
      return new Long(readLong());
    } else if (type == short.class) {
      return new Short(readShort());
    }

    return readObject();
  }

  public void serializeValue(Object value, Class type)
      throws SerializationException {
    if (type == boolean.class) {
      writeBoolean(((Boolean) value).booleanValue());
    } else if (type == byte.class) {
      writeByte(((Byte) value).byteValue());
    } else if (type == char.class) {
      writeChar(((Character) value).charValue());
    } else if (type == double.class) {
      writeDouble(((Double) value).doubleValue());
    } else if (type == float.class) {
      writeFloat(((Float) value).floatValue());
    } else if (type == int.class) {
      writeInt(((Integer) value).intValue());
    } else if (type == long.class) {
      writeLong(((Long) value).longValue());
    } else if (type == short.class) {
      writeShort(((Short) value).shortValue());
    } else {
      writeObject(value);
    }
  }

  protected String getInstanceName(String token) {
    int nameIndex = Integer.parseInt(token.substring(1));
    return getString(nameIndex);
  }

  public Object readObject() throws SerializationException {
    return deserialize();
  }

  public void writeObject(Object value) throws SerializationException {
    serialize(value);
  }

  private void deserializeClass(Object instance, Class instanceClass)
      throws IllegalArgumentException, IllegalAccessException,
      SerializationException {

    rememberDecodedObject(instance);

    Field[] declFields = instanceClass.getDeclaredFields();
    Field[] serializableFields = serializableTypeOracle
      .applyFieldSerializationPolicy(declFields);

    for (int index = 0; index < serializableFields.length; ++index) {
      Field declField = serializableFields[index];
      assert (declField != null);

      // Supress access restrictions
      boolean isAccessible = declField.isAccessible();
      declField.setAccessible(true);

      Object value = deserializeValue(declField.getType());

      declField.set(instance, value);

      // Restore access restrictions
      declField.setAccessible(isAccessible);
    }

    Class superClass = instanceClass.getSuperclass();
    if (superClass != null && IsSerializable.class.isAssignableFrom(superClass)) {
      deserializeClass(instance, superClass);
    }
  }

  private Object deserialize() throws SerializationException {
    SerializationStreamObjectDecoder decoder = getObjectDecoder();
    Object instance = decoder.prevInstance(this);
    if (instance != null) {
      return instance;
    }

    String encodedInstanceRef = decoder.nextInstance(this);
    if (encodedInstanceRef == null) {
      return null;
    }

    SerializedInstanceReference serializedInstRef = serializableTypeOracle
      .decodeSerializedInstanceReference(encodedInstanceRef);

    try {
      Class instanceClass = Class.forName(serializedInstRef.getName(), false,
        this.getClass().getClassLoader());

      if (!serializableTypeOracle.isSerializable(instanceClass)) {
        throw new SerializationException("Class '" + instanceClass.getName()
          + "' is not serializable");
      }

      Class customSerializer = serializableTypeOracle
        .hasCustomFieldSerializer(instanceClass);

      validateTypeVersions(instanceClass, serializedInstRef);

      if (customSerializer != null) {
        instance = deserizeWithCustomSerializer(customSerializer, instanceClass);
      } else {
        // Arrays are dealt with by the use of custom serializers, so array
        // types should never reach this point in the code.
        //
        assert (!instanceClass.isArray());
        instance = instanceClass.newInstance();
        deserializeClass(instance, instanceClass);
      }

      return instance;

    } catch (ClassNotFoundException e) {
      throw new SerializationException(e);

    } catch (InstantiationException e) {
      throw new SerializationException(e);

    } catch (IllegalAccessException e) {
      throw new SerializationException(e);

    }
  }

  private Object deserizeWithCustomSerializer(Class customSerializer,
                                              Class instanceClass) throws SerializationException {
    Object instance = null;

    try {
      try {
        Method instantiate = customSerializer.getMethod("instantiate",
          new Class[]{SerializationStreamReader.class});

        instance = instantiate.invoke(null, new Object[]{this});

      } catch (NoSuchMethodException e) {
        // purposely ignored

      }

      if (instance == null) {
        if (instanceClass.isArray()) {
          int length = readInt();
          Class componentType = instanceClass.getComponentType();
          instance = Array.newInstance(componentType, length);

          if (!componentType.isPrimitive()) {
            instanceClass = Class.forName("[Ljava.lang.Object;");
          }

        } else {
          instance = instanceClass.newInstance();
        }
      }

      rememberDecodedObject(instance);

      Method deserialize = customSerializer.getMethod("deserialize",
        new Class[]{SerializationStreamReader.class, instanceClass});

      deserialize.invoke(null, new Object[]{this, instance});

      return instance;

    } catch (SecurityException e) {
      throw new SerializationException(e);

    } catch (NoSuchMethodException e) {
      throw new SerializationException(e);

    } catch (IllegalArgumentException e) {
      throw new SerializationException(e);

    } catch (IllegalAccessException e) {
      throw new SerializationException(e);

    } catch (InvocationTargetException e) {
      throw new SerializationException(e);

    } catch (InstantiationException e) {
      throw new SerializationException(e);

    } catch (ClassNotFoundException e) {
      throw new SerializationException(e);

    }
  }

  private void validateTypeVersions(Class instanceClass,
                                    SerializedInstanceReference serializedInstRef)
      throws SerializationException {
    String clientTypeSignature = serializedInstRef.getSignature();
    if (clientTypeSignature.length() == 0) {
      if (shouldEnforceTypeVersioning()) {
        // TODO(mmendez): add a more descriptive error message here
        throw new SerializationException();
      }

      return;
    }

    String serverTypeSignature = serializableTypeOracle
      .getSerializationSignature(instanceClass);

    if (!clientTypeSignature.equals(serverTypeSignature)) {
      throw new SerializationException("Invalid type signature for "
        + instanceClass.getName());
    }
  }

  private void serialize(Object instance) throws SerializationException {
    com.google.gwt.user.client.rpc.SerializationStreamObjectEncoder encoder = getObjectEncoder();
    if (encoder.startInstance(this, instance)) {
      serializeImpl(instance, instance.getClass());
    }
    encoder.endInstance(this, instance);
  }

  private void serializeImpl(Object instance, Class instanceClass)
      throws SerializationException {
    assert (instance != null);

    Class customSerializer = serializableTypeOracle
      .hasCustomFieldSerializer(instanceClass);

    if (customSerializer != null) {
      serializeWithCustomSerializer(customSerializer, instance, instanceClass);
    } else {
      // Arrays are serialized using custom serializers so we should never get
      // here for array types.
      //
      assert (!instanceClass.isArray());
      serializeClass(instance, instanceClass);
    }
  }

  private void serializeWithCustomSerializer(Class customSerializer,
                                             Object instance, Class instanceClass) throws SerializationException {

    Method serialize;
    try {
      if (instanceClass.isArray()) {
        Class componentType = instanceClass.getComponentType();
        if (!componentType.isPrimitive()) {
          instanceClass = Class.forName("[Ljava.lang.Object;");
        }
      }

      serialize = customSerializer.getMethod("serialize", new Class[]{
        SerializationStreamWriter.class, instanceClass});

      serialize.invoke(null, new Object[]{this, instance});

    } catch (SecurityException e) {
      throw new SerializationException(e);

    } catch (NoSuchMethodException e) {
      throw new SerializationException(e);

    } catch (IllegalArgumentException e) {
      throw new SerializationException(e);

    } catch (IllegalAccessException e) {
      throw new SerializationException(e);

    } catch (InvocationTargetException e) {
      throw new SerializationException(e);

    } catch (ClassNotFoundException e) {
      throw new SerializationException(e);

    }
  }

  private void serializeClass(Object instance, Class instanceClass)
      throws SerializationException {
    assert (instance != null);

    Field[] declFields = instanceClass.getDeclaredFields();
    Field[] serializableFields = serializableTypeOracle
      .applyFieldSerializationPolicy(declFields);
    for (int index = 0; index < serializableFields.length; ++index) {
      Field declField = serializableFields[index];
      assert (declField != null);

      boolean isAccessible = declField.isAccessible();
      declField.setAccessible(true);

      Object value;
      try {
        value = declField.get(instance);
        serializeValue(value, declField.getType());

      } catch (IllegalArgumentException e) {
        throw new SerializationException(e);

      } catch (IllegalAccessException e) {
        throw new SerializationException(e);

      }

      declField.setAccessible(isAccessible);
    }

    Class superClass = instanceClass.getSuperclass();
    if (superClass != null && IsSerializable.class.isAssignableFrom(superClass)) {
      serializeImpl(instance, superClass);
    }
  }

  public StringTable getStringTable() {
    return this;
  }

  public int getStringCount() {
    if (fStringTable == null) {
      return fStringsSeen.size();
    }

    return fStringTable.length;
  }
}
