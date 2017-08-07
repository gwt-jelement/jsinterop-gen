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

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class VelocityLogger implements LogChute {
    private final Logger logger;

    public VelocityLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void init(RuntimeServices runtimeServices) throws Exception {
        //no default implementation
    }

    @Override
    public void log(int level, String message) {
        if (isLevelEnabled(level)) {
            switch(level) {
                case DEBUG_ID:
                case INFO_ID:
                case WARN_ID:
                    logger.debug(() -> message);
                    break;
                case ERROR_ID:
                    logger.reportError(message);
                    break;
            }
        }
    }

    @Override
    public void log(int level, String message, Throwable throwable) {
        if (isLevelEnabled(level)) {
            logger.formatError("ERROR: %s", message);
        }
    }

    @Override
    public boolean isLevelEnabled(int level) {
        switch (level) {
            case TRACE_ID:
                return false;
            case DEBUG_ID:
            case INFO_ID:
                return logger.getLogLevel() >= Logger.LEVEL_DEBUG;
            case WARN_ID:
            case ERROR_ID:
                return true;
        }
        return true;
    }
}
