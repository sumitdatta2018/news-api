package com.mydomain.newsapi;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.RepresentationModel;

public final class TestUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String RESOURCE_PATH = "src/test/resources/";
  private static final String REQUEST_PATH = "http/request/";
  private static final String RESPONSE_PATH = "http/response/";

  @SneakyThrows
  public static <T> T readRequestJson(final String path, final Class<T> clazz) {
    var json = readResource(REQUEST_PATH + path);
    return OBJECT_MAPPER.readValue(json, clazz);
  }

  @SneakyThrows
  public static <T> T readResponseJson(final String path, final Class<T> clazz) {
    var json = readResource(RESPONSE_PATH + path);
    return OBJECT_MAPPER.readValue(json, clazz);
  }

  @SneakyThrows
  public static <T extends RepresentationModel<? extends T>> Object readResponse(final String path,
      final Class<T> clazz) {
    var json = readResource(RESPONSE_PATH + path);
    return OBJECT_MAPPER.readValue(json, new TypeReference<RepresentationModel<T>>() {});
  }

  @SneakyThrows
  public static <T> T readTypeResponse(final String path, final Class<T> clazz) {
    var json = readResource(RESPONSE_PATH + path);
    return OBJECT_MAPPER.readValue(json, new TypeReference<T>() {});
  }

  @SneakyThrows
  public static String readResource(final String path) {
    return Files.readString(Paths.get(RESOURCE_PATH + path));
  }

  public static String readRequestFile(final String fileName) {
    return readResource(REQUEST_PATH + fileName);
  }

  public static String readResponseFile(final String fileName) {
    return readResource(RESPONSE_PATH + fileName);
  }

  private TestUtil() {}
}
