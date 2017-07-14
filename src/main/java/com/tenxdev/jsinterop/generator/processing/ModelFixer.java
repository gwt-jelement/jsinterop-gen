package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModelFixer {

    private final Model model;

    public ModelFixer(Model model) {
        this.model=model;
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
        List<Method> methods= ((InterfaceDefinition) model.getDefinitionInfo("USBDevice").getDefinition()).getMethods();
        Optional<Method> methodToFix = methods.stream().filter(method -> "isochronousTransferOut".equals(method.getName())).findFirst();
        if (methodToFix.isPresent()){
            methodToFix.get().getReturnTypes()[0]="Promise<USBIsochronousInTransferResult>";
        }
    }
}
