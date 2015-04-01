/*
 * Copyright 2015 PayU
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
package com.payu.ratel.register;

/**
 * This interface represents a strategy of registering a service in the service
 * registry.
 *
 */
public interface RegisterStrategy {

  /**
   * Register service identified by a name under given address
   * 
   * @param name
   *          name of the service to be used in the service registry. Multiple
   *          instances of the same service must share the name.
   * @param address
   *          absolute address of the service endpoint
   */
  void registerService(String name, String address);
}
