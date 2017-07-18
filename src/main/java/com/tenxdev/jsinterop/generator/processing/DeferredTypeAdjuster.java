package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.stream.Collectors;

public class DeferredTypeAdjuster extends AbstractTypeVisitor<Type> {

    private final TypeFactory typeFactory;

    DeferredTypeAdjuster(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    @Override
    public Type accept(Type type) {
        return type == null ? new NativeType("Object") : super.accept(type);
    }

    @Override
    protected Type visitArrayType(ArrayType type) {
        return new ArrayType(accept(type.getType()));
    }

    @Override
    protected Type visitUnionType(UnionType type) {
        return new UnionType(type.getName(), type.getTypes().stream()
                .map(this::accept)
                .collect(Collectors.toList()));
    }

    @Override
    protected Type visitParameterizedType(ParameterizedType type) {
        return new ParameterizedType (accept(type.getBaseType()),
                type.getTypeParameters().stream()
                        .map(this::accept)
                        .collect(Collectors.toList()));
    }

    @Override
    protected Type visitEnumType(EnumType type) {
        return type;
    }

    @Override
    protected Type visitObjectType(ObjectType type) {
        return type;
    }

    @Override
    protected Type visitNativeType(NativeType type) {
        return typeFactory.getTypeNoParse(type.getTypeName());
    }
}
