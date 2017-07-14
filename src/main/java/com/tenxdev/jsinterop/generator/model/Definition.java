package com.tenxdev.jsinterop.generator.model;

import java.util.List;
import java.util.Set;

public interface Definition {

    String getName();

    boolean isPartial();

    Set<String> getTypeUsage();
}
