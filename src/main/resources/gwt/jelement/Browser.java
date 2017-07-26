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

package gwt.jelement;

import gwt.jelement.frame.Location;
import gwt.jelement.frame.Navigator;
import gwt.jelement.frame.Window;
import gwt.jelement.html.HTMLDocument;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;


@JsType(name = "window", isNative = true, namespace = JsPackage.GLOBAL)
public class Browser {
    public static Navigator navigator;
    public static Window window;
    public static HTMLDocument document;
    public static Location location;
    public static Window self;
    public static Window top;
    public static double Infinity;
    public static double NaN;
    public static Object undefined;
    public static gwt.jelement.core.Math JsMath;

    public static native Object eval(String code);

    public static native String uneval(Object object);

    public static native boolean isFinite(Object object);

    public static native boolean isNaN(Object object);

    public static native double parseFloat(Object object);

    public static native double parseInt(Object object);

    public static native String decodeURI(String value);

    public static native String decodeURIComponent(String component);

    public static native String encodeURI(String uri);

    public static native String encodeURIComponent(String uriComponent);

    @Deprecated
    public static native String escape(String value);

    @Deprecated
    public static native String unescape(String value);
}