package com.tenxdev.jsinterop.generator.processing.unionargsexampansion;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.List;

public class HasUnionTypeVisitor extends AbstractTypeVisitor<Boolean> {


    @Override
    protected Boolean visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected Boolean visitUnionType(UnionType type) {
        return true;
    }

    @Override
    protected Boolean visitParameterizedType(ParameterizedType type) {
        return accept(type.getBaseType()) ||
                type.getTypeParameters().stream()
                        .anyMatch(this::accept);
    }

    @Override
    protected Boolean visitEnumType(EnumType type) {
        return false;
    }

    @Override
    protected Boolean visitObjectType(ObjectType type) {
        return false;
    }

    @Override
    protected Boolean visitNativeType(NativeType type) {
        return false;
    }
}
