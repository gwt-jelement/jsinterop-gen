package com.tenxdev.jsinterop.generator;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.errors.PrintStreamErrorrHandler;
import com.tenxdev.jsinterop.generator.generator.SourceGenerator;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.parsing.ModelBuilder;
import com.tenxdev.jsinterop.generator.processing.*;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    @Option(name = "-in", usage = "the folder from which to read IDL files", required = true, metaVar = "inputDirectory")
    private String inputDirectory;

    @Option(name = "-out", usage = "the folder in which the Java project will be created", required = true, metaVar = "outputDirectory")
    private String outputDirectory;

    @Option(name = "-package", usage = "the base package into which to place the IDL files", metaVar = "basePackage")
    private String basePackage = "gwt.jelement";

    @Option(name = "-force", usage = "renove the output folder if it exissts", metaVar = "force")
    private boolean force;

    @Option(name = "-overwrite", usage = "overwrite the output folder if it exissts", metaVar = "overwrite")
    private boolean overwrite;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        new Generator().checkArguments(args);
    }

    private void checkArguments(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            checkArguments();
            ErrorReporter errorHandler = new PrintStreamErrorrHandler(System.err);
            Model model = new ModelBuilder(errorHandler).buildFrom(inputDirectory);
            processModel(model, errorHandler);
            System.exit(0);
        } catch (CmdLineException e) {
            printUsage(parser, e);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void processModel(Model model, ErrorReporter errorHandler) throws IOException {
        TypeMapper typeMapper = new TypeMapper(model, errorHandler);
        //ordering of these operations is critical
        new ModelFixer(model, errorHandler).processModel();
        new TypeDefsProcessor(model).processModel(typeMapper);
        new PartialsMerger(model, errorHandler).processModel();
        new ImplementsMerger(model, errorHandler).processModel();
        new MethodUnionArgsExpander(model, typeMapper).processModel();
        new MethodOptionalArgsExpander(model).processModel();
        new ImportResolver().processModel(model);
        new SourceGenerator().processModel(model, outputDirectory, basePackage, errorHandler);
    }

    private void checkArguments() throws CmdLineException {
        if (basePackage.endsWith(".")) {
            basePackage = removeLastCharacter(basePackage);
        }
        if (outputDirectory.endsWith("/")) {
            outputDirectory = removeLastCharacter(outputDirectory);
        }
        if (force && overwrite) {
            throw new CmdLineException(null, "Cannot specifiy both -force and -overwrite", null);
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

    String removeLastCharacter(String value) {
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
                throw new CmdLineException(null, String.format("Could not empty output direcctory '%s'%n", outputDirectory), null);
            }
            try {
                MoreFiles.deleteDirectoryContents(output.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
            } catch (IOException e) {
                throw new CmdLineException(null, String.format("Could not empty output direcctory '%s': %s%n", outputDirectory, e.getMessage()), e);
            }
        } else {
            throw new CmdLineException(null, String.format("Output directory '%s' exists and -force was not specified.%n", outputDirectory), null);
        }
    }

    private void printUsage(CmdLineParser parser, CmdLineException e) {
        System.err.println(e.getMessage());
        System.err.println("java Generator [options...]");
        parser.printUsage(System.err);
        System.err.println();
    }


}