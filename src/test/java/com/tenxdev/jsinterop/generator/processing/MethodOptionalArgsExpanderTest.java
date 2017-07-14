package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MethodOptionalArgsExpanderTest {
    @org.junit.Test
    public void expandMethod() throws Exception {
        List<MethodArgument> arguments = Arrays.asList(
                new MethodArgument("a", new String[]{"int"}, false, false, null),
                new MethodArgument("b", new String[]{"int"}, false, true, null),
                new MethodArgument("c", new String[]{"int"}, false, true, null)
        );
        Method method = new Method("test", new String[]{"void"}, arguments, false);

        List<Method> expandedMethods = new MethodOptionalArgsExpander(null).expandMethod(method);

        assertEquals(3, expandedMethods.size());
        assertEquals(1, expandedMethods.get(0).getArguments().size());
        assertEquals(2, expandedMethods.get(1).getArguments().size());
        assertEquals(3, expandedMethods.get(2).getArguments().size());
        assertEquals("a",expandedMethods.get(0).getArguments().get(0).getName());
        assertEquals("a",expandedMethods.get(1).getArguments().get(0).getName());
        assertEquals("b",expandedMethods.get(1).getArguments().get(1).getName());
        assertEquals("a",expandedMethods.get(2).getArguments().get(0).getName());
        assertEquals("b",expandedMethods.get(2).getArguments().get(1).getName());
        assertEquals("c",expandedMethods.get(2).getArguments().get(2).getName());
    }
}