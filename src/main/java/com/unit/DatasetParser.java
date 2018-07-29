package com.unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DatasetParser {
    List<DataSetEntry> createDatasetFromFile(final String inputFileName) throws FileNotFoundException {
        File file = null;
        try {
            // loading from resources first
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(inputFileName).getFile());
        } catch (Exception e) {
            file = new File(inputFileName);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        }
        Scanner scanner = new Scanner(file);
        List<DataSetEntry> dataSetEntries = new ArrayList<>();
        // Skip headers
        scanner.next();
        while (scanner.hasNext()) {
            try {
                String[] res = scanner.next().split(",");
                dataSetEntries.add(new DataSetEntry(Double.parseDouble(res[0]), Double.parseDouble(res[1])));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Parsing file error!");
                System.exit(1);
            }
        }
        return dataSetEntries;
    }

    static final class DataSetEntry {
        private final double x;
        private final double y;

        DataSetEntry(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double getX() {
            return x;
        }

        double getY() {
            return y;
        }
    }
}
