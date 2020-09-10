package com.waes.test.utils;

import java.io.File;
import java.io.IOException;

import static java.util.Base64.getEncoder;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class BinaryConversionUtil {

    public static byte[] getBinaryAsBase64ByteArray(String path) {
        return binaryToBase64String(path).getBytes();
    }

    public static String getBinaryAsBase64String(String path) {
        return binaryToBase64String(path);
    }

    private static String binaryToBase64String(String path) {
        String hoHandsBinaryPath = BinaryConversionUtil.class.getClassLoader().getResource(path).getPath();
        byte[] fileContent;
        try {
            fileContent = readFileToByteArray(new File(hoHandsBinaryPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file to byte array");
        }
        return getEncoder().encodeToString(fileContent);
    }
}
