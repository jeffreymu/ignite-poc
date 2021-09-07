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
 * An interface for audit operations.
 * <p>
 * Different implementations may have specific strategies to derive the actual operation string.
 */
public interface AuditOperation {
    /**
     * Provide the operation string for this operation.
     *
     * @return the operation as a string
     */
    String getOperationString();

    /**
     * Provide the operation string for this operation without bound values being appended. This applies to
     * prepared statement operations.
     *
     * @return the operation without bound values as a string
     */
    String getNakedOperationString();
}
