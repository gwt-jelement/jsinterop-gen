package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.types.Type;

public abstract class AbstractTypeVisitor<T> {

    public abstract T accept(Type type);
}
