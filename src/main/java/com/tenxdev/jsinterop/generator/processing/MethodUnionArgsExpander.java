package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class generates new methods for methods with union type arguments, and removes the definition of methods with
 * union types
 * for example, given foo( (HTMLImageElement or SVGImageElement or HTMLVideoElement ) image, double x, double y)
 * The following methods will be created, keeping a pointer to the original method, and the original method will be
 * removed from the definition:
 * - foo(HTMLImageElement image, double x, double y)
 * - foo(SVGImageElement image, double x, double y)
 * - foo(HTMLVideoElement image, double x, double y)
 */
public class MethodUnionArgsExpander {
    private final Model model;

    public MethodUnionArgsExpander(Model model) {
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

    private List<Method> processMethods(List<Method> methods){
        List<Method> newMethods = new ArrayList<>();
        methods.forEach(method -> processMethod(method, newMethods));
        return newMethods;
    }

    private void processMethod(Method method, List<Method> newMethods) {
        Optional<MethodArgument> argument = method.getArguments().stream()
                .filter(arg-> arg.getType() instanceof UnionType)
                .findFirst();
        if (argument.isPresent()) {
            for (Method newMethod : processArgument(method, argument.get())) {
                processMethod(newMethod, newMethods);
            }
        } else {
            newMethods.add(method);
        }
    }

    private List<Method> processArgument(Method method, MethodArgument argument) {
        List<Method> newMethods=new ArrayList<>();
        for (Type type : ((UnionType)argument.getType()).getTypes()) {
            List<MethodArgument> newArguments = method.getArguments().stream().map(arg ->
                    new MethodArgument(arg.getName(), arg.equals(argument) ? type : arg.getType(),
                            arg.isVararg(), arg.isOptional(), arg.getDefaultValue())).collect(Collectors.toList());
            Method newMethod=new Method(method.getName(), method.getReturnType(), newArguments, method.isStatic());
            newMethods.add(newMethod);
        }
        return newMethods;
    }



}
