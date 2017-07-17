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

        ObjectType that = (ObjectType) o;

        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectType{" +
                "packageName='" + packageName + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
