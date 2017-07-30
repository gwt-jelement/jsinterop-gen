package com.tenxdev.jsinterop.generator.generator

class JsGenerator extends XtendTemplate {

    def generate(String basePackage){
        return '''
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

package «basePackage».core;

public class Js {

    public static native <T> JsObject<T> with(JsObject<T> object, String key, T value)/*-{
        object[key] = value;
        return object;
    }-*/;

    «FOR String primitiveType: GWT_PRIMITIVE_TYPES»
        public static native <T> JsObject<T> with(JsObject<T> object, String key, «primitiveType» value)/*-{
            object[key] = value;
            return object;
        }-*/;

    «ENDFOR»
    public static <T> JsObject<T> with(JsObject<T> object, String key, long value){
        return with(object, key, (double) value);
    };

    public static native <T> void set(JsObject<T> object, String propertyName, T value) /*-{
        object[propertyName] = value;
    }-*/;

    «FOR String primitiveType: GWT_PRIMITIVE_TYPES»
        public static native void set(JsObject object, String propertyName, «primitiveType» value) /*-{
            object[propertyName] = value;
        }-*/;

    «ENDFOR»
    public static void set(JsObject object, String propertyName, long value) {
        set(object, propertyName, (double) value);
    }

    public static native <T> void set(JsObject<T> object, double index, T value) /*-{
        object[index] = value;
    }-*/;

    «FOR String primitiveType: GWT_PRIMITIVE_TYPES»
        public static native <T> void set(JsObject<T> object, double index, «primitiveType» value) /*-{
            object[index] = value;
        }-*/;

    «ENDFOR»
    public static void set(JsObject object, double index, long value) {
        set(object, index, (double) value);
    }

    public static native <T> T get(JsObject<T> object, String propertyName) /*-{
        return objobject[propertyName];
    }-*/;

    public static native <T> T get(JsObject<T> object, double index) /*-{
        return object[index];
    }-*/;

    «FOR String primitiveType: GWT_PRIMITIVE_TYPES»
        public static native «primitiveType» get«primitiveType.toFirstUpper»(JsObject object, String propertyName) /*-{
            return object[propertyName];
        }-*/;

        public static native «primitiveType» get«primitiveType.toFirstUpper»(JsObject object, double index) /*-{
            return object[index];
        }-*/;

    «ENDFOR»
    public static long getLong(JsObject object, String propertyName) {
        return (long) getDouble(object, propertyName);
    }

    public static long getLong(JsObject object, double index) {
        return (long) getDouble(object, index);
    }

    public static native <T> boolean has(JsObject<T> object, String propertyName) /*-{
        return propertyName in object;
    }-*/;

    public static native <T> boolean has(JsObject<T> object, double index) /*-{
        return index in object;
    }-*/;

    public static native <T> void delete(JsObject<T> object, String propertyName) /*-{
        delete object[propertyName];
    }-*/;

    public static native <T> void delete(JsObject<T> object, double index) /*-{
        delete object[index];
    }-*/;

    public static native boolean isTrue(Object object)/*-{
        return !!object;
    }-*/;

    public static native boolean exists(Object object)/*-{
        return !!object;
    }-*/;

    public static native boolean isFalse(Object object)/*-{
        return !object;
    }-*/;

    public static native boolean isEqual(Object object1, Object object2)/*-{
        return object1 == object2;
    }-*/;

    public static native boolean isStrictlyEqual(Object object1, Object object2)/*-{
        return object1 === object2;
    }-*/;

}
        '''
    }
}