package com.tenxdev.jsinterop.generator.processing.enumarguments;

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

public class HasEnumTypeVisitor extends AbstractTypeVisitor<Boolean> {
    @Override
    protected Boolean visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected Boolean visitUnionType(UnionType type) {
        return type.getTypes().stream()
                .anyMatch(this::accept);
    }

    @Override
    protected Boolean visitParameterisedType(ParameterisedType type) {
        return type.getTypeParameters().stream()
                .anyMatch(this::accept);
    }

    @Override
    protected Boolean visitEnumType(EnumType type) {
        return true;
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
