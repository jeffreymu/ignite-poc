/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package me.jeff.ignitepoc.chronicle.events;

import java.io.Serializable;

/**
 * Basic interface for creation of Event objects.
 *
 * @author Erwin Wagasow
 * Created by Erwin Wagasow on 17.09.2018
 */
public interface EventBuilder<T extends Serializable> extends Serializable {

    /**
     * Builds an instance of the desired type
     *
     * @return a new instance of the desired type
     */
    T build();
}
