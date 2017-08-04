/*
 * Copyright 2017 Abed Tony BenBrahim <tony.benrahim@10xdev.com>
 *     and Gwt-JElement project contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tenxdev.jsinterop.generator;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.tenxdev.jsinterop.generator.generator.SourceGenerator;
import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.logging.PrintStreamLogger;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.processing.*;
import com.tenxdev.jsinterop.generator.processing.enumtypes.AttributeEnumTypeProcessor;
import com.tenxdev.jsinterop.generator.processing.enumtypes.DictionaryMemberEnumTypeProcessor;
import com.tenxdev.jsinterop.generator.processing.enumtypes.MethodEnumArgumentProcessor;
import com.tenxdev.jsinterop.generator.processing.generictypes.GenericTypesProcessor;
import com.tenxdev.jsinterop.generator.processing.packageusage.ImportResolver;
import com.tenxdev.jsinterop.generator.processing.uniontypes.AttributeUnionTypeProcessor;
import com.tenxdev.jsinterop.generator.processing.uniontypes.DictionaryMemberUnionTypeProcessor;
import com.tenxdev.jsinterop.generator.processing.uniontypes.MethodUnionArgsExpander;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Generator {

    @SuppressWarnings("unused")
    @Option(name = "-in", usage = "the folder from which to read IDL files", required = true, metaVar = "inputDirectory")
    private String inputDirectory;

    @Option(name = "-out", usage = "the folder in which the Java project will be created", required = true, metaVar = "outputDirectory")
    private String outputDirectory;

    @Option(name = "-package", usage = "the base package into which to place the IDL files", metaVar = "basePackage")
    private String basePackage = "gwt.jelement";

    @SuppressWarnings("unused")
    @Option(name = "-force", usage = "remove the output folder if it exists")
    private boolean force;

    @SuppressWarnings("unused")
    @Option(name = "-overwrite", usage = "overwrite the output folder if it exists")
    private boolean overwrite;

    @SuppressWarnings("FieldCanBeLocal")
    @Option(name = "-logLevel", usage = "logging level: 0-no logging 1-mimum logging", metaVar = "logLevel")
    private int logLevel = 1;

    @SuppressWarnings("unused")
    @Argument
    private List<String> arguments = new ArrayList<>();

    public static void main(String[] args) {
        new Generator().execute(args);
    }

    private void execute(String[] args) {
        Logger logger = new PrintStreamLogger(System.out, System.err);
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            logger.setLogLevel(logLevel);
            banner(logger);
            checkArguments();
            Model model = new ModelBuilder(logger).buildFrom(inputDirectory);
            processModel(model, logger);
            System.exit(0);
        } catch (CmdLineException e) {
            printUsage(parser, logger, e);
            System.exit(-1);
        } catch (IOException e) {
            logger.reportError(e.getMessage());
            System.exit(-1);
        }
    }

    private void processModel(Model model, Logger logger) throws IOException {
        //ordering of these operations is critical
        new NamedConstructorHandler(model, logger).process(); //adds new definitions
        new PartialsMerger(model, logger).processModel();
        new RedundantImplementsRemoval().process(model, logger); //before implements merger
        new ImplementsMerger(model, logger).processModel(); //must run after partials merger

        new DuplicateInheritedDictionaryMemberRemover(model, logger).process();
        new DuplicateInheritedInterfaceMembersRemover(model, logger).process();

        new GenericTypesProcessor(model, logger).process();
        new MethodUnionArgsExpander(model, logger).processModel(); //must run after all interface merging
        new MethodOptionalArgsExpander(model, logger).processModel();//must run after union args expansion
        new MethodEnumArgumentProcessor(model, logger).process(); // must run after all method expansions
        new NativeTypeArrayArgumentHandler(model, logger).process();

        new AttributeUnionTypeProcessor(model, logger).process();
        new AttributeEnumTypeProcessor(model, logger).process();

        new DictionaryMemberUnionTypeProcessor(model, logger).process();
        new DictionaryMemberEnumTypeProcessor(model, logger).process();

        new SuperCallConstructorProcessor(model, logger).process();//after method expansion
        new AttributeConflictingOverlayRemover(model, logger).process();

        new InterfaceDetector(model, logger).process();

        new ImportResolver().processModel(model, logger); //must run after all type substitutions
        new SourceGenerator(logger).processModel(model, outputDirectory, basePackage);
    }

    private void checkArguments() throws CmdLineException {
        if (basePackage.endsWith(".")) {
            basePackage = removeLastCharacter(basePackage);
        }
        if (outputDirectory.endsWith("/")) {
            outputDirectory = removeLastCharacter(outputDirectory);
        }
        if (force && overwrite) {
            throw new CmdLineException(null, "Cannot specify both -force and -overwrite", null);
        }
        File output = new File(outputDirectory);
        if (output.exists()) {
            if (!overwrite) {
                clearOutputDirectory(output);
            }
        } else {
            createOutputDirectory(output);
        }
    }

    private String removeLastCharacter(String value) {
        return value.substring(0, value.length() - 1);
    }

    private void createOutputDirectory(File output) throws CmdLineException {
        if (!output.mkdirs()) {
            throw new CmdLineException(null, String.format("Could not create output directory '%s'%n", outputDirectory), null);
        }
    }

    private void clearOutputDirectory(File output) throws CmdLineException {
        if (force) {
            //sanity check, we should find pom.xml in the folder, if not have the user delete the folder manually,
            //we do not want to delete the user's hard drive
            File checkFile = new File(output, "pom.xml");
            if (!checkFile.exists()) {
                throw new CmdLineException(null, String.format("Could not empty output directory '%s'%n", outputDirectory), null);
            }
            try {
                MoreFiles.deleteDirectoryContents(output.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
            } catch (IOException e) {
                throw new CmdLineException(null, String.format("Could not empty output directory '%s': %s%n", outputDirectory, e.getMessage()), e);
            }
        } else {
            throw new CmdLineException(null, String.format("Output directory '%s' exists and -force was not specified.%n", outputDirectory), null);
        }
    }

    private void printUsage(CmdLineParser parser, Logger logger, CmdLineException e) {
        logger.reportError(e.getMessage());
        logger.reportError("java Generator [options...]");
        parser.printUsage(System.out);
    }

    private void banner(Logger logger) {
        logger.rawOutput(() -> "       __     ____      __                                             \n" +
                "      / /____/  _/___  / /____  _________  ____        ____ ____  ____ \n" +
                " __  / / ___// // __ \\/ __/ _ \\/ ___/ __ \\/ __ \\______/ __ `/ _ \\/ __ \\\n" +
                "/ /_/ (__  )/ // / / / /_/  __/ /  / /_/ / /_/ /_____/ /_/ /  __/ / / /\n" +
                "\\____/____/___/_/ /_/\\__/\\___/_/   \\____/ .___/      \\__, /\\___/_/ /_/ \n" +
                "                                       /_/          /____/             ");
    }

}