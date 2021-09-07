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
package me.jeff.ignitepoc.chronicle.audit;

import com.google.common.base.Splitter;
import me.jeff.ignitepoc.chronicle.common.FieldSelector;
import net.openhft.chronicle.queue.RollCycle;
import net.openhft.chronicle.queue.RollCycles;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ChronicleAuditLoggerConfig {
    private static final String CONFIG_LOG_DIR = "log_dir";
    private static final String CONFIG_ROLL_CYCLE = "roll_cycle";
    private static final String CONFIG_MAX_LOG_SIZE = "max_log_size";
    private static final String CONFIG_FIELDS = "fields";
    private static final long DEFAULT_MAX_LOG_SIZE = 16L * 1024L * 1024L * 1024L; // 16 GB

    private final Path logPath;
    private final RollCycle rollCycle;
    private final long maxLogSize;
    private final FieldSelector fieldSelector;


    ChronicleAuditLoggerConfig(Map<String, String> parameters) {
        logPath = resolveLogPath(parameters);
        rollCycle = resolveRollCycle(parameters);
        maxLogSize = resolveMaxLogSize(parameters);
        fieldSelector = resolveFields(parameters);
    }

    private static Path resolveLogPath(Map<String, String> parameters) {
        mandatoryConfig(CONFIG_LOG_DIR, parameters);
        try {
            return Paths.get(parameters.get(CONFIG_LOG_DIR));
        } catch (InvalidPathException e) {
            throw new RuntimeException("Invalid chronicle logger log directory path: " + parameters.get(CONFIG_LOG_DIR), e);
        }
    }

    private static RollCycle resolveRollCycle(Map<String, String> parameters) {
        try {
            return Optional.ofNullable(parameters.get(CONFIG_ROLL_CYCLE))
                    .map(RollCycles::valueOf)
                    .orElse(RollCycles.HOURLY);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid chronicle logger roll cycle: " + parameters.get(CONFIG_ROLL_CYCLE), e);
        }
    }

    private long resolveMaxLogSize(Map<String, String> parameters) {
        long size;
        try {
            size = Optional.ofNullable(parameters.get(CONFIG_MAX_LOG_SIZE))
                    .map(Long::valueOf)
                    .orElse(DEFAULT_MAX_LOG_SIZE);
        } catch (NumberFormatException e) {
            throw new RuntimeException("...");
        }

        if (size <= 0) {
            throw new RuntimeException("Invalid chronicle logger max log size: " + parameters.get(CONFIG_MAX_LOG_SIZE));
        }

        return size;
    }

    private static void mandatoryConfig(String option, Map<String, String> parameters) {
        if (!parameters.containsKey(option)) {
            throw new RuntimeException("Chronicle logger backend require '" + option + "' parameter option");
        }
    }

    private FieldSelector resolveFields(Map<String, String> parameters) {
        String fieldsString = parameters.getOrDefault(CONFIG_FIELDS, "");

        List<String> fields = Splitter.on(",")
                .trimResults()
                .omitEmptyStrings()
                .splitToList(fieldsString);
        if (fields.isEmpty()) {
            return FieldSelector.DEFAULT_FIELDS;
        }
        try {
            return FieldSelector.fromFields(fields);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid chronicle logger fields: " + fieldsString, e);
        }
    }

    public Path getLogPath() {
        return logPath;
    }

    public RollCycle getRollCycle() {
        return rollCycle;
    }

    public long getMaxLogSize() {
        return maxLogSize;
    }

    public FieldSelector getFields() {
        return fieldSelector;
    }
}
