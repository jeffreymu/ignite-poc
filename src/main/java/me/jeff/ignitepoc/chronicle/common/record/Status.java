/*
 * Copyright 2018 Telefonaktiebolaget LM Ericsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jeff.ignitepoc.chronicle.common.record;

/**
 * Indicates the status of an operation record.
 */
public enum Status {
    /**
     * Operation is about to be executed.
     */
    ATTEMPT("attempt"),
    /**
     * Operation executed and failed.
     */
    FAILED("failed"),
    /**
     * Operation executed and succeeded.
     */
    SUCCEEDED("succeeded");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
