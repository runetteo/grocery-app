package com.magenic.masters.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FileUtil {

    private static final String FILE_DIR = "resources/";

    public static String readFile(String fileName) {
        Path filepath = Paths.get(FILE_DIR + fileName);
        try {
            return Files.readString(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void saveToFile(String contents, String fileName) {
        Path filePath = Paths.get(FILE_DIR + fileName);
        try {
            Files.writeString(filePath, contents, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
