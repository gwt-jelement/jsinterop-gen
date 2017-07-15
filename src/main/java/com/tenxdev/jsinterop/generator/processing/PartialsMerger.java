package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.*;

public class PartialsMerger extends AbstractDefinitionMerger {
    private final Model model;

    public PartialsMerger(Model model, ErrorReporter errorHandler) {
        super(errorHandler);
        this.model = model;
    }

    public void processModel() {
        processPartials();
    }

    private void processPartials() {
        model.getDefinitions().stream().filter(info -> !info.getPartialDefinitions().isEmpty()).forEach(definitionInfo -> {
            Definition primaryDefinition = definitionInfo.getDefinition();
            for (PartialDefinition partialDefinition : definitionInfo.getPartialDefinitions()) {
                if (partialDefinition instanceof PartialInterfaceDefinition && primaryDefinition instanceof InterfaceDefinition) {
                    mergeInterfaces((InterfaceDefinition) primaryDefinition, (InterfaceDefinition) partialDefinition);
                } else if (partialDefinition instanceof PartialDictionaryDefinition && primaryDefinition instanceof DictionaryDefinition) {
                    mergeDicstionaries((DictionaryDefinition) primaryDefinition, (DictionaryDefinition) partialDefinition);
                } else {
                    reportTypeMismatch(primaryDefinition, partialDefinition);
                }
            }
        });
    }

}
