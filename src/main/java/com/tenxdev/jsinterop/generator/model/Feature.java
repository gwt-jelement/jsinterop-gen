package com.tenxdev.jsinterop.generator.model;

import java.util.Arrays;

public class Feature implements InterfaceMember {

    private final FeatureType featureType;
    private final boolean readOnly;
    String[] valueTypes;
    String[] keyTypes;

    public Feature(FeatureType featureType, String[] valueTypes, boolean readOnly) {
        this(featureType, null, valueTypes, readOnly);
    }

    public Feature(FeatureType featureType, String[] keyTypes, String[] valueTypes, boolean readOnly) {
        this.featureType = featureType;
        this.valueTypes = valueTypes;
        this.keyTypes = keyTypes;
        this.readOnly = readOnly;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String[] getValueTypes() {
        return valueTypes;
    }

    public String[] getKeyTypes() {
        return keyTypes;
    }

    public boolean isPairIterator() {
        return keyTypes != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (readOnly != feature.readOnly) return false;
        if (featureType != feature.featureType) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(valueTypes, feature.valueTypes)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(keyTypes, feature.keyTypes);
    }

    @Override
    public int hashCode() {
        int result = featureType != null ? featureType.hashCode() : 0;
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + Arrays.hashCode(valueTypes);
        result = 31 * result + Arrays.hashCode(keyTypes);
        return result;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "featureType=" + featureType +
                ", readOnly=" + readOnly +
                ", valueType='" + (valueTypes == null ? null : Arrays.asList(valueTypes)) + '\'' +
                ", keyType='" + (keyTypes == null ? null : Arrays.asList(keyTypes)) + '\'' +
                '}';
    }

    public enum FeatureType {ValueIterator, MapIterator, MapLike, SetLike}
}
