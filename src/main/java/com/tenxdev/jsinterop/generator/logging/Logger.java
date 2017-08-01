/*
 * Copyright 2017 Abed Tony BenBrahim <tony.benrahim@10xdev.com>
 *     and Gwt-JElement project contributors.
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

package com.tenxdev.jsinterop.generator.logging;

import java.io.OutputStream;
import java.util.function.Supplier;

public interface Logger {

    int LEVEL_ERROR = 0;

    int LEVEL_INFO = 1;

    int LEVEL_DEBUG = 2;

    void reportError(String error);

    void formatError(String format, Object... args);

    OutputStream getPrintStream();

    int getLogLevel();

    void setLogLevel(int logLevel);

    void info(Supplier<String> messageSupplier);

    void debug(Supplier<String> messageSupplier);

    void rawOutput(Supplier<String> messageSupplier);
}
