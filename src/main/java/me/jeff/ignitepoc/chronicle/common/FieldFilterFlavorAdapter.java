/*
 * Copyright 2019 Telefonaktiebolaget LM Ericsson
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
package me.jeff.ignitepoc.chronicle.common;


import me.jeff.ignitepoc.chronicle.common.record.AuditRecord;

/**
 * This logic is extracted to a flavor specific adapter.
 * <p>
 * ecAudit comes in different flavors, one for each supported Cassandra version.
 * Flavor adapters encapsulates differences between flavors and simplifies maintenance.
 */
final class FieldFilterFlavorAdapter {
    private FieldFilterFlavorAdapter() {
        // Utility class
    }

    static FieldSelector getFieldsAvailableInRecord(AuditRecord auditRecord, FieldSelector configuredFields) {
        FieldSelector fields = configuredFields;

        if (!auditRecord.getBatchId().isPresent()) {
            fields = fields.withoutField(FieldSelector.Field.BATCH_ID);
        }

        if (!auditRecord.getSubject().isPresent()) {
            fields = fields.withoutField(FieldSelector.Field.SUBJECT);
        }

        if (auditRecord.getClientAddress() == null) {
            fields = fields.withoutField(FieldSelector.Field.CLIENT_IP).withoutField(FieldSelector.Field.CLIENT_PORT);
        }

        return fields;
    }
}
