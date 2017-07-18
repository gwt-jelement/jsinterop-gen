package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

public class Feature implements InterfaceMember {

    private final FeatureType featureType;
    private final boolean readOnly;
    private final Type valueType;
    private final Type keyType;

    public Feature(FeatureType featureType, Type valueType, boolean readOnly) {
        this(featureType, null, valueType, readOnly);
    }

    public Feature(FeatureType featureType, Type keyType, Type valueType, boolean readOnly) {
        this.featureType = featureType;
        this.valueType = valueType;
        this.keyType = keyType;
        this.readOnly = readOnly;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Type getValueType() {
        return valueType;
    }

    public Type getKeyType() {
        return keyType;
    }

    public boolean isPairIterator() {
        return keyType != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (readOnly != feature.readOnly) return false;
        if (featureType != feature.featureType) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!valueType.equals(feature.valueType)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return keyType.equals(feature.keyType);
    }

    @Override
    public int hashCode() {
        int result = featureType != null ? featureType.hashCode() : 0;
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + valueType.hashCode();
        result = 31 * result + keyType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "featureType=" + featureType +
                ", readOnly=" + readOnly +
                ", valueType='" + valueType + '\'' +
                ", keyType='" + keyType + '\'' +
                '}';
    }

    public enum FeatureType {VALUE_ITERATOR, MAP_ITERATOR, MAP_LIKE, SET_LIKE}
}
