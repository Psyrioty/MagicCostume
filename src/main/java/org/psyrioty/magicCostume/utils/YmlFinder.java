package org.psyrioty.magicCostume.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YmlFinder {
    public static List<File> findYamlFiles(File folder) {
        List<File> result = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) return result;

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(findYamlFiles(file));
            } else if (file.getName().endsWith(".yml")) {
                result.add(file);
            }
        }

        return result;
    }
}
