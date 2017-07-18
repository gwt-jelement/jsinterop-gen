package com.tenxdev.jsinterop.generator.processing.unionargsexampansion;

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetUnionTypesVisitor extends AbstractTypeVisitor<List<UnionType>> {
    @Override
    protected List<UnionType> visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected List<UnionType> visitUnionType(UnionType type) {
        List<UnionType> result = new ArrayList<>();
        result.add(type);
        result.addAll(type.getTypes().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<UnionType> visitParameterisedType(ParameterisedType type) {
        List<UnionType> result = new ArrayList<>();
        result.addAll(accept(type.getBaseType()));
        result.addAll(type.getTypeParameters().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    protected List<UnionType> visitEnumType(EnumType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<UnionType> visitObjectType(ObjectType type) {
        return Collections.emptyList();
    }

    @Override
    protected List<UnionType> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }
}
