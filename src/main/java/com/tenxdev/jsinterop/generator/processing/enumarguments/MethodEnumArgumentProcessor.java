package com.tenxdev.jsinterop.generator.processing.enumarguments;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;

public class MethodEnumArgumentProcessor {

    private final HasEnumTypeVisitor hasEnumTypeVisitor = new HasEnumTypeVisitor();

    public void process(Model model) {
        Type substitutionType = model.getTypeFactory().getType("any");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition() instanceof InterfaceDefinition)
                .map(definitionInfo -> (InterfaceDefinition) definitionInfo.getDefinition())
                .forEach(interfaceDefinition -> processInterfaceDefinition(interfaceDefinition, substitutionType));
    }

    private void processInterfaceDefinition(InterfaceDefinition interfaceDefinition, Type substitutionType) {
        List<Method> newMethods = new ArrayList<>();
        interfaceDefinition.getMethods().forEach(method -> {
            Method newMethod = processMethod(method, substitutionType);
            if (newMethod != null) {
                newMethods.add(newMethod);
            }
        });
        interfaceDefinition.getMethods().addAll(newMethods);
    }

    private Method processMethod(Method method, Type substitutionType) {
        boolean hasEnumTypes = false;
        List<MethodArgument> newArguments = new ArrayList<>();
        Method newMethod = method.newMethodWithArguments(newArguments);
        for (MethodArgument argument : method.getArguments()) {
            if (hasEnumTypeVisitor.accept(argument.getType())) {
                MethodArgument newMethodArgument = new MethodArgument(argument.getName(), substitutionType,
                        argument.isVararg(), argument.isOptional(), argument.getDefaultValue());
                newMethodArgument.setEnumSubstitution(true);
                newArguments.add(newMethodArgument);
                hasEnumTypes = true;
                newMethod.setPrivate(true);
                method.setEnumOverlay(newMethod);
            } else {
                newArguments.add(argument);
            }
        }
        return hasEnumTypes ? newMethod : null;
    }
}
