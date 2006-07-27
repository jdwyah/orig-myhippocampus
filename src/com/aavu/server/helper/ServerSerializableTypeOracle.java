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

import java.lang.reflect.Field;

import com.google.gwt.user.rebind.rpc.SerializedInstanceReference;

/**
 * This interface defines a serializable type oracle that can be used by the rpc
 * servlet.
 * @skip
 */
interface ServerSerializableTypeOracle {

  /**
   * Given a set of fields for a given instance type, return an array of fields
   * that represents the fields that can actually be serialized.
   * @param declaredFields
   * @return
   */
  Field[] applyFieldSerializationPolicy(Field[] declaredFields);

  /**
   * Given an encoded serialized instance reference, return an object
   * that
   * @param encodedSerializedInstanceReference
   * @return
   */
  SerializedInstanceReference decodeSerializedInstanceReference(
      String encodedSerializedInstanceReference);

  /**
   * Given an instance type generate an encoded string that represents the
   * serialized instance.
   *
   * @param instanceType
   * @return
   */
  String encodeSerializedInstanceReference(Class instanceType);

  /**
   * Get the serialization signature for a given instance type.
   *
   * @param instanceType
   * @return
   */
  String getSerializationSignature(Class instanceType);

  /**
   * Get the serialized name of a type instance.
   *
   * @param instanceType
   * @return
   */
  String getSerializedTypeName(Class instanceType);

  /**
   * Return the class object for the custom field serializer for a given
   * instance type.
   *
   * @param qualifiedTypeName
   * @return Class object for the custom field serializer for the instance type
   *         or null if there is no custom field serializer
   */
  Class hasCustomFieldSerializer(Class instanceType);

  /**
   * Returns true if the instance type is serializable.
   *
   * @param instanceType
   * @return true if the instance type is serializable
   */
  boolean isSerializable(Class instanceType);
}
