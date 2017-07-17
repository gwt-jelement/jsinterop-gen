package com.tenxdev.jsinterop.generator.processing.visitors;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Union;
import com.tenxdev.jsinterop.generator.model.types.*;

public abstract class AbstractTypeVisitor<T> {

    public T accept(Type type){
        if (type instanceof NativeType){
            return visitNativeType((NativeType) type);
        }
        if (type instanceof ObjectType){
            return visitObjectType((ObjectType) type);
        }
        if (type instanceof EnumType){
            return visitEnumType((EnumType) type);
        }
        if (type instanceof ParameterizedType){
            return visitParameterizedType((ParameterizedType)type);
        }
        if (type instanceof UnionType){
            return visitUnionType((UnionType) type);
        }
        if (type instanceof ArrayType){
            return visitArrayType((ArrayType) type);
        }
        throw new IllegalStateException("Unknown type "+type.getClass().getName());
    }

    protected abstract T visitArrayType(ArrayType type);

    protected abstract T visitUnionType(UnionType type);

    protected abstract T visitParameterizedType(ParameterizedType type);

    protected abstract T visitEnumType(EnumType type);

    protected abstract T visitObjectType(ObjectType type);

    protected abstract T visitNativeType(NativeType type);
}
