package com.tenxdev.jsinterop.generator;

import com.tenxdev.jsinterop.generator.errors.PrintStreamErrorrHandler;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.processing.FileListBuilder;
import com.tenxdev.jsinterop.generator.processing.ModelBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Generator {

    @Option(name = "-in", usage = "the folder from which to read IDL files", required = true, metaVar = "inputDirectory")
    private String inputDirectory;

    @Option(name = "=package", usage = "the base package into which to place the IDL files", metaVar = "basePackage")
    private String basePackage = "gwtproject.jsinterop.generated";

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        new Generator().checkArguments(args);
    }

    private void checkArguments(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            ErrorReporter errorHandler=new PrintStreamErrorrHandler(System.err);
            List<File> fileList=new FileListBuilder(errorHandler).findFiles(inputDirectory);
            Map<String, DefinitionInfo> model=new ModelBuilder(errorHandler).buildFrom(inputDirectory, fileList);
            System.exit(0);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java Generator [options...]");
            parser.printUsage(System.err);
            System.err.println();
            System.exit(-1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }








}