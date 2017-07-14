package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.ArrayList;
import java.util.List;

public class MethodOptionalArgsExpander {

    private final Model model;

    public MethodOptionalArgsExpander(Model model) {
        this.model=model;
    }

    public void processModel() {
        model.getDefinitions().forEach(definitionInfo -> {
            if (definitionInfo.getDefinition() instanceof  InterfaceDefinition){
                processInterface((InterfaceDefinition) definitionInfo.getDefinition());
            }
        });
    }

    private void processInterface(InterfaceDefinition definition) {
        List<Method> newMethods = new ArrayList<>();
        definition.setExpandedMethods(processMethods(definition.getMethods()));
        definition.setExpandedConstructors(processMethods(definition.getConstructors()));
    }

    private List<Method> processMethods(List<Method> methods) {
        List<Method> newMethods=new ArrayList<>();
        methods.forEach(method -> {
            if (hasOptionalArgs(method)) {
                newMethods.addAll(expandMethod(method));
            } else {
                newMethods.add(method);
            }
        });
        return newMethods;
    }

    private boolean hasOptionalArgs(Method method) {
        return method.getArguments().stream().anyMatch(MethodArgument::isOptional);
    }

    List<Method> expandMethod(Method method) {
        List<Method> expandedMethods = new ArrayList<>();
        expandMethod(method, expandedMethods);
        return expandedMethods;
    }

    private void expandMethod(Method method, List<Method> expandedMethods) {
        boolean hasOptionsl = false;
        List<MethodArgument> newArguments = new ArrayList<>();
        for (MethodArgument argument : method.getArguments()) {
            if (argument.isOptional() && !hasOptionsl) {
                Method newMethod = new Method(method.getName(), method.getReturnTypes(), new ArrayList<>(newArguments), method.isStatic());
                expandedMethods.add(newMethod);
                hasOptionsl = true;
                newArguments.add(new MethodArgument(argument.getName(), argument.getTypes(), argument.isVararg(), false, argument.getDefaultValue()));
            } else {
                newArguments.add(argument);
            }
        }
        Method newMethod = new Method(method.getName(), method.getReturnTypes(), newArguments, method.isStatic());
        if (hasOptionsl) {
            expandMethod(newMethod, expandedMethods);
        } else {
            expandedMethods.add(newMethod);
        }
    }

}
