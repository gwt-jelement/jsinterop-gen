package com.tenxdev.jsinterop.generator.processing.enumtypes;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.types.EnumType;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;

public class MethodEnumArgumentProcessor {

    private final HasEnumTypeVisitor hasEnumTypeVisitor = new HasEnumTypeVisitor();
    private Model model;
    private Logger logger;

    public MethodEnumArgumentProcessor(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Processing methods with enum arguments");
        model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition() instanceof InterfaceDefinition)
                .map(definitionInfo -> (InterfaceDefinition) definitionInfo.getDefinition())
                .forEach(interfaceDefinition ->
                        processInterfaceDefinition(interfaceDefinition));
    }

    private void processInterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        List<Method> newMethods = new ArrayList<>();
        interfaceDefinition.getMethods().forEach(method -> {
            Method newMethod = processMethod(method);
            if (newMethod != null) {
                newMethods.add(newMethod);
            }
        });
        interfaceDefinition.getMethods().addAll(newMethods);
    }

    private Method processMethod(Method method) {
        boolean hasEnumTypes = false;
        List<MethodArgument> newArguments = new ArrayList<>();
        Method newMethod = method.newMethodWithArguments(newArguments);
        for (MethodArgument argument : method.getArguments()) {
            if (hasEnumTypeVisitor.accept(argument.getType())) {
                Type substitutionType = new EnumSubstitutionVisitor(model, logger).accept(argument.getType());
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
        if (hasEnumTypeVisitor.accept(method.getReturnType())) {
            Type newReturnType = new EnumSubstitutionVisitor(model, logger).accept(method.getReturnType());
            if (method.getReturnType() instanceof EnumType) {
                hasEnumTypes = true;
                newMethod.setReturnType(newReturnType);
                newMethod.setPrivate(true);
                method.setEnumOverlay(newMethod);
                method.setEnumReturnType(true);
            } else {
                method.setReturnType(newReturnType);
            }
        }
        return hasEnumTypes ? newMethod : null;
    }
}
