package com.tenxdev.jsinterop.generator.model;

public class Feature implements InterfaceMember {

    private final FeatureType featureType;
    private final boolean readOnly;
    String valueType;
    String keyType;
    public Feature(FeatureType featureType, String valueType, boolean readOnly) {
        this(featureType, null, valueType, readOnly);
    }

    public Feature(FeatureType featureType, String keyType, String valueType, boolean readOnly) {
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

    public String getValueType() {
        return valueType;
    }

    public String getKeyType() {
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
        if (valueType != null ? !valueType.equals(feature.valueType) : feature.valueType != null) return false;
        return keyType != null ? keyType.equals(feature.keyType) : feature.keyType == null;
    }

    @Override
    public int hashCode() {
        int result = featureType.hashCode();
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        result = 31 * result + (keyType != null ? keyType.hashCode() : 0);
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

    public enum FeatureType {ValueIterator, MapIterator, MapLike, SetLike}
}
