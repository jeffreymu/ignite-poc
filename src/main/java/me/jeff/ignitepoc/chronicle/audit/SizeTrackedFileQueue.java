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

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

class SizeTrackedFileQueue {
    private final Queue<File> releasedStoreFiles = new LinkedList<>();
    private long bytesInStoreFiles;

    long accumulatedFileSize() {
        return bytesInStoreFiles;
    }

    void offer(File file) {
        releasedStoreFiles.offer(file);
        // Not accurate because the files are sparse, but it's at least pessimistic
        bytesInStoreFiles += file.length();
    }

    File poll() {
        File file = releasedStoreFiles.poll();

        if (file != null) {
            bytesInStoreFiles -= file.length();
        }

        return file;
    }

    void clear() {
        releasedStoreFiles.clear();
        bytesInStoreFiles = 0;
    }
}
