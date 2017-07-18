package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TypeVisitor extends AbstractTypeVisitor<List<String>> {

    @Override
    public List<String> accept(Type type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return super.accept(type);
    }

    @Override
    protected List<String> visitArrayType(ArrayType type) {
        return accept(type.getType());
    }

    @Override
    protected List<String> visitUnionType(UnionType type) {
        return type.getTypes().stream()
                .map(this::accept)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected List<String> visitParameterisedType(ParameterisedType type) {
        List<String> result = new ArrayList<>();
        result.addAll(accept(type.getBaseType()));
        for (Type parameterType : type.getTypeParameters()) {
            result.addAll(accept(parameterType));
        }
        return result;
    }

    @Override
    protected List<String> visitEnumType(EnumType type) {
        return Collections.singletonList(type.getPackageName() + "." + type.getTypeName());
    }

    @Override
    protected List<String> visitObjectType(ObjectType type) {
        return Collections.singletonList(type.getPackageName() + "." + type.getTypeName());
    }

    @Override
    protected List<String> visitNativeType(NativeType type) {
        return Collections.emptyList();
    }
}
