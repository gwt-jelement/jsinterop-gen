package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.types.PackageType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractTypeVisitor;

import java.util.Collections;
import java.util.List;

public class TypeVisitor extends AbstractTypeVisitor<List<String>> {


    @Override
    public List<String> accept(Type type) {
        return type instanceof PackageType ?
                Collections.singletonList(((PackageType) type).getPackageName()) :
                Collections.emptyList();

    }
}
