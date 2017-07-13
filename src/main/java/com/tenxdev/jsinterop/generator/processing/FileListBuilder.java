package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListBuilder {

    private final ErrorReporter errorHandler;

    public FileListBuilder(ErrorReporter errorHandler) {
        this.errorHandler=errorHandler;
    }

    public List<File> findFiles(String baseDirectory) {
        File directory = new File(baseDirectory);
        if (!directory.exists()) {
            errorHandler.formatFatalError("Input directory %s does not exist.%n", baseDirectory);
        }
        List<File> fileList = new ArrayList<>();
        findFiles(directory, fileList);
        if (fileList.isEmpty()){
            errorHandler.formatFatalError("No idl files were found in input directory %s%n", baseDirectory);
        }
        return fileList;
    }

    private void findFiles(File baseDirectory, List<File> fileList) {
        File[] files = baseDirectory.listFiles((dir, name) -> {
            File file = new File(dir, name);
            return file.isDirectory() || (file.isFile() && name.toLowerCase().endsWith(".idl"));
        });
        for (File file : files) {
            if (file.isDirectory()) {
                findFiles(file, fileList);
            } else {
                fileList.add(file);
            }
        }
    }
}
