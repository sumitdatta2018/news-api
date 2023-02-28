package com.mydomain.newsapi.tests.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

  public static final String DATA_FILES_RESOURCE_LOCATION = "./src/test/resources/data";

  public static String readDataFile(String fileName) throws IOException {
    return Files.readString(Paths.get(DATA_FILES_RESOURCE_LOCATION, fileName));
  }
}

