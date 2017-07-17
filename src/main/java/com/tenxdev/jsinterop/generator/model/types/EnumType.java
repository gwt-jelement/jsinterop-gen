package com.tenxdev.jsinterop.generator.model.types;

public class EnumType extends ObjectType {

    public EnumType(String name, String packageName) {
        super(name, packageName);
    }

    @Override
    public String toString() {
        return "EnumType{" +
                "packageName='" + getPackageName()+ '\'' +
                '}';
    }

    @Override
    public String displayValue() {
        return getTypeName();
    }
}
