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

package com.tenxdev.jsinterop.generator.generator;

import java.util.Arrays;
import java.util.function.Function;

public enum MarginFxer {

    INSTANCE;

    public String fix(String input) {
        if (input.isEmpty()) {
            return input;
        }
        String[] inputLines = input.split("\n");
        int smallestMargin = Arrays.stream(inputLines)
                .filter(line -> !line.trim().isEmpty())
                .map(line -> line.indexOf(line.trim()))
                .min((o1, o2) -> o1 - o2)
                .map(Function.identity())
                .orElse(0);
        if (smallestMargin == 0) {
            return input;
        }
        return fixupMargin(inputLines, smallestMargin);
    }

    private String fixupMargin(String[] inputLines, int smallestMargin) {
        String marginToDelete = new String(new char[smallestMargin]).replace('\0', ' ');
        StringBuilder result = new StringBuilder();
        boolean startedOutput = false;
        for (String inputLine : inputLines) {
            if (startedOutput || !inputLine.trim().isEmpty()) {
                startedOutput = true;
                if (inputLine.indexOf(marginToDelete) == 0) {
                    result.append(inputLine.substring(smallestMargin)).append('\n');
                } else {
                    result.append(inputLine).append('\n');
                }
            }
        }
        return result.toString();
    }
}
