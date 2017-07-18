package com.tenxdev.jsinterop.generator.processing;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypeUtilTest {

    @Test
    public void checkParameterisedTypes() throws Exception {
        Set<String> types = TypeUtil.INSTANCE.checkParameterisedTypes("sequence<Foo>");
        assertEquals(2, types.size());
        assertTrue(types.contains("sequence"));
        assertTrue(types.contains("Foo"));
    }

}
