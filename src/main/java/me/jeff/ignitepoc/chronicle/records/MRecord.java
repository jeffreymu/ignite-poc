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

package me.jeff.ignitepoc.chronicle.records;

import me.jeff.ignitepoc.chronicle.values.dates.Value;

import java.util.Collection;
import java.util.Date;

public interface MRecord extends CRecord {

    /**
     * Getter
     *
     * @return Source Identifier
     */
    String getSource();

    /**
     * Typed Nullable Getter
     *
     * @param channel Channel to extract
     * @return Channel Value as double
     */
    Double getDouble(String channel);

    /**
     * Typed Nullable Getter
     *
     * @param channel Channel to extract
     * @return Channel value as Long
     */
    Long getLong(String channel);

    /**
     * Typed Nullable Getter
     *
     * @param channel Channel to extract
     * @return Channel Value as Boolean
     */
    Boolean getBoolean(String channel);

    /**
     * Typed Nullable Getter
     *
     * @param channel Channel to extract
     * @return Channel Value as Date
     */
    Date getDate(String channel);

    /**
     * Typed Nullable Getter
     *
     * @param channel Channel to extract
     * @return Channel Value as String
     */
    String getString(String channel);

    /**
     * Getter
     *
     * @param channel Channel to Extract
     * @return Channel Value as {@link Value}
     */
    Value getValue(String channel);

    /**
     * Untyped Getter, use with care!
     *
     * @param channel Channel to extract
     * @return Channel Value as {@link Object}
     */
    Object get(String channel);

    /**
     * Return a Collection of all channels in this Record.
     * @return Collection with all channels.
     */
    Collection<String> getChannels();

}
