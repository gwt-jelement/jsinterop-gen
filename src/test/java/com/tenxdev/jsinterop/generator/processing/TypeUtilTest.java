package com.tenxdev.jsinterop.generator.processing;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Set;

public class TypeUtilTest {

    @Test
    public void checkParameterizedTypes() throws Exception {
        Set<String> types = TypeUtil.INSTANCE.checkParameterizedTypes("sequence<Foo>");
        assertEquals(2, types.size());
        assertTrue(types.contains("sequence"));
        assertTrue(types.contains("Foo"));
    }

}
