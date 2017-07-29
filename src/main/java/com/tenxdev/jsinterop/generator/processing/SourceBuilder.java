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

package com.tenxdev.jsinterop.generator.processing;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SourceBuilder {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final StringBuilder stringBuilder;
    private final String indent;

    public SourceBuilder(int numSpacesPerIndent) {
        this.indent = new String(new char[numSpacesPerIndent]).replace("\0", " ");
        this.stringBuilder = new StringBuilder();
    }

    public SourceBuilder(SourceBuilder sourceBuilder) {
        this.indent = sourceBuilder.indent;
        this.stringBuilder = sourceBuilder.stringBuilder;
    }

    public SourceBuilder indent(int level) {
        for (int i = 0; i < level; ++i) {
            stringBuilder.append(indent);
        }
        return this;
    }

    private SourceBuilder newLine() {
        stringBuilder.append(LINE_SEPARATOR);
        return this;
    }

    private SourceBuilder append(String value) {
        stringBuilder.append(value);
        return this;
    }

    public SourceBuilder appendIf(boolean condition, Supplier<String> valueSupplier) {
        if (condition) {
            append(valueSupplier.get());
        }
        return this;
    }

    public SourceBuilder appendIf(boolean condition, Supplier<String> valueTrueSupplier, Supplier<String> valueFalseSupplier) {
        if (condition) {
            append(valueTrueSupplier.get());
        } else {
            append(valueFalseSupplier.get());
        }
        return this;
    }

    public SourceBuilder newLineOnlyIf(boolean condition) {
        if (condition) {
            newLine();
        }
        return this;
    }

    public SourceBuilder consume(Consumer<SourceBuilder> sourceBuilderConsumer) {
        sourceBuilderConsumer.accept(this);
        return this;
    }

    public String getText() {
        return stringBuilder.toString();
    }

    public CharSequence asCharSequence() {
        return stringBuilder;
    }
}
