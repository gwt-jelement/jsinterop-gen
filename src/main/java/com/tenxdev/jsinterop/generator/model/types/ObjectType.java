package com.tenxdev.jsinterop.generator.model.types;

public class ObjectType implements Type, PackageType{
    private final String packageName;
    private final String typeName;

    public ObjectType(String typeName, String packageName){
        this.typeName=typeName;
        this.packageName=packageName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String displayValue() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ObjectType that = (ObjectType) o;

        return packageName.equals(that.packageName);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + packageName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ObjectType{" +
                "packageName='" + packageName + '\'' +
                '}';
    }
}
