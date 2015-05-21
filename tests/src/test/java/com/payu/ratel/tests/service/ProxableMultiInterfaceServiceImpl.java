/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.tests.service;

import com.payu.ratel.Publish;

@Publish(value = Test2Service.class)
public class ProxableMultiInterfaceServiceImpl implements ProxableService, Test2Service {

    @Override
    public int doInTransaction() {
        return 4;
    }

    @Override
    public String helloWorld() {
        return "hello world";
    }

    @Override
    public int power(Integer arg) {
        return arg * arg;
    }
}
