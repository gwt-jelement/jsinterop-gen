package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelFixer {

    private final Model model;
    private final ErrorReporter errorReporter;

    public ModelFixer(Model model, ErrorReporter errorReporter) {
        this.model = model;
        this.errorReporter=errorReporter;
    }

    public void processModel() {
        /*
        URLSearchParams.idl
        cannot be parsed by current parser:
        Constructor(optional (sequence<sequence<USVString>> or record<USVString, USVString> or USVString) init = "")
         */
        Method constructor = ((InterfaceDefinition) model.getDefinitionInfo("URLSearchParams").getDefinition()).getConstructors().get(0);
        constructor.getArguments().clear();
        MethodArgument argument = new MethodArgument("init",
                new String[]{"sequence<sequence<USVString>>", "record<USVString, USVString>", "USVString"},
                false, true, "\"\"");
        constructor.getArguments().add(argument);

        /*
            USBDevice.idl
            UsbIsochronousOutTransferResult is mispelled in IDL file, should be USBIsochronousOutTransferResult
         */
        List<Method> methods = ((InterfaceDefinition) model.getDefinitionInfo("USBDevice").getDefinition()).getMethods();
        Optional<Method> methodToFix = methods.stream().filter(method -> "isochronousTransferOut".equals(method.getName())).findFirst();
        if (methodToFix.isPresent()) {
            methodToFix.get().getReturnTypes()[0] = "Promise<USBIsochronousInTransferResult>";
        }
        /*
            Add EventHnadler type
         */
        List<MethodArgument> arguments = Arrays.asList(new MethodArgument("event", new String[]{"Event"}, false, false, null));
        CallbackDefinition eventHandlerDefinition = new CallbackDefinition("EventHandler",
                new Method(null,new String[]{"Event"}, arguments, false));
        try {
            model.registerDefinition(eventHandlerDefinition,"","");
        } catch (Model.ConflictingNameExcepton conflictingNameExcepton) {
            errorReporter.reportError("Skipped addition of EventHandler, alreaady registered");
        }
    }
}
