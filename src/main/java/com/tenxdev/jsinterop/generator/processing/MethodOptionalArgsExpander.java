package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates nee methods without optional arguments for methods with optional arguments, and removes all method
 * definitions with optional arguments.
 * For example, given foo(a, b?, c?)
 * the following methods will be added:
 * foo(a)
 * foo(a, b)
 * foo(a, b, c)
 * and the original method will be removed
 */
public class MethodOptionalArgsExpander {

    private final Model model;

    public MethodOptionalArgsExpander(Model model) {
        this.model = model;
    }

    public void processModel() {
        model.getDefinitions().forEach(definitionInfo -> {
            if (definitionInfo.getDefinition() instanceof InterfaceDefinition) {
                processInterface((InterfaceDefinition) definitionInfo.getDefinition());
            }
        });
    }

    private void processInterface(InterfaceDefinition definition) {
        List<Method> newMethods = processMethods(definition.getMethods());
        definition.getMethods().clear();
        definition.getMethods().addAll(newMethods);

        List<Method> newConstructors = processMethods(definition.getConstructors());
        definition.getConstructors().clear();
        definition.getConstructors().addAll(newConstructors);
    }

    private List<Method> processMethods(List<Method> methods) {
        List<Method> newMethods = new ArrayList<>();
        methods.forEach(method -> {
            if (hasOptionalArgs(method)) {
                List<Method> expandedMethods = expandMethod(method);
                expandedMethods.removeAll(newMethods);
                newMethods.addAll(expandedMethods);
            } else {
                newMethods.add(method);
            }
        });
        return newMethods;
    }

    private boolean hasOptionalArgs(Method method) {
        return method.getArguments().stream().anyMatch(MethodArgument::isOptional);
    }

    private List<Method> expandMethod(Method method) {
        List<Method> expandedMethods = new ArrayList<>();
        expandMethod(method, expandedMethods);
        return expandedMethods;
    }

    private void expandMethod(Method method, List<Method> expandedMethods) {
        boolean hasOptions = false;
        List<MethodArgument> newArguments = new ArrayList<>();
        for (MethodArgument argument : method.getArguments()) {
            if (argument.isOptional() && !hasOptions) {
                Method newMethod = new Method(method.getName(), method.getReturnType(), new ArrayList<>(newArguments), method.isStatic());
                expandedMethods.add(newMethod);
                hasOptions = true;
                newArguments.add(new MethodArgument(argument.getName(), argument.getType(), argument.isVararg(), false, argument.getDefaultValue()));
            } else {
                newArguments.add(argument);
            }
        }
        Method newMethod = new Method(method.getName(), method.getReturnType(), newArguments, method.isStatic());
        if (hasOptions) {
            expandMethod(newMethod, expandedMethods);
        } else {
            expandedMethods.add(newMethod);
        }
    }

}
