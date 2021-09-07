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
package me.jeff.ignitepoc.chronicle.audit;

import me.jeff.ignitepoc.chronicle.common.record.AuditOperation;
import me.jeff.ignitepoc.chronicle.common.record.AuditRecord;
import me.jeff.ignitepoc.chronicle.common.record.Status;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

/**
 * The domain object which contains audit record information to be logged.
 * <p>
 * Instances are immutable an may only be created using the {@link AuditEntry.Builder}.
 */
public class AuditEntry implements AuditRecord {
    public static final int UNKNOWN_PORT = 0;

    private final InetSocketAddress clientAddress;
    private final InetAddress coordinatorAddress;
    private final AuditOperation operation;
    private final String user;
    private final UUID batchId;
    private final Status status;
    private final Long timestamp;
    private final String subject;
    private final boolean hasKnownOperation;

    /**
     * @see #newBuilder()
     */
    private AuditEntry(Builder builder) {
        this.clientAddress = builder.client;
        this.coordinatorAddress = builder.coordinator;
        this.operation = builder.operation;
        this.user = builder.user;
        this.batchId = builder.batchId;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
        this.subject = builder.subject;
        this.hasKnownOperation = builder.hasKnownOperation;
    }

    @Override
    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    @Override
    public InetAddress getCoordinatorAddress() {
        return coordinatorAddress;
    }

    /**
     * Gets the operation in this value object.
     * <p>
     * As the operation string may be relatively expensive to produce it may be calculated lazily
     * depending on the concrete implementation used.
     *
     * @return the audit operation
     */
    @Override
    public AuditOperation getOperation() {
        return operation;
    }

    @Override
    public String getUser() {
        return user;
    }

    /**
     * Gets the optional batch id in this value object.
     *
     * @return the batch id
     */
    @Override
    public Optional<UUID> getBatchId() {
        return Optional.ofNullable(batchId);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * @return the timestamp when this entry was created. Represented by the number of milliseconds since Epoch.
     */
    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public Optional<String> getSubject() {
        return Optional.ofNullable(subject);
    }

    /**
     * @return True if the statement was parsed successfully.
     */
    public boolean hasKnownOperation() {
        return hasKnownOperation;
    }

    /**
     * Create a new {@link Builder} instance.
     *
     * @return a new instance of {@link Builder}.
     */
    public static AuditEntry.Builder newBuilder() {
        return new Builder();
    }

    /**
     * Implements a builder of {@link AuditEntry}'s.
     */
    public static class Builder {
        private InetSocketAddress client;
        private InetAddress coordinator;
        private AuditOperation operation;
        private String user;
        private UUID batchId;
        private Status status;
        private Long timestamp;
        private String subject;
        private boolean hasKnownOperation = true;

        public Builder client(InetSocketAddress address) {
            this.client = address;
            return this;
        }

        public Builder coordinator(InetAddress coordinator) {
            this.coordinator = coordinator;
            return this;
        }

        /**
         * Set the audit operation that is to be logged.
         * <p>
         * As the operation string may be relatively expensive to produce it may be calculated lazily
         * depending on the concrete implementation used.
         *
         * @param operation the audit operation
         * @return this builder instance
         */
        public Builder operation(AuditOperation operation) {
            this.operation = operation;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Set the optional batch identifier.
         *
         * @param uuid the batch id to use
         * @return this builder instance
         */
        public Builder batch(UUID uuid) {
            this.batchId = uuid;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder knownOperation(boolean hasKnownOperation) {
            this.hasKnownOperation = hasKnownOperation;
            return this;
        }

        /**
         * Configure this builder from an existing {@link AuditEntry} instance.
         *
         * @param entry the instance to get values from
         * @return this builder instance
         */
        public Builder basedOn(AuditEntry entry) {
            this.client = entry.getClientAddress();
            this.coordinator = entry.getCoordinatorAddress();
            this.operation = entry.getOperation();
            this.user = entry.getUser();
            this.batchId = entry.getBatchId().orElse(null);
            this.status = entry.getStatus();
            this.timestamp = entry.getTimestamp();
            this.subject = entry.getSubject().orElse(null);
            this.hasKnownOperation = entry.hasKnownOperation();
            return this;
        }

        /**
         * Build a {@link AuditEntry} instance as configured by this builder.
         *
         * @return an {@link AuditEntry} instance
         */
        public AuditEntry build() {
            return new AuditEntry(this);
        }
    }
}
