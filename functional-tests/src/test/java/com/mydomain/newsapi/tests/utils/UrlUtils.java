package com.mydomain.newsapi.tests.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
  public static URL create(String base, String endpoint) throws MalformedURLException {
    String baseWithoutTrailingSlash = base.replaceAll("/$", "");
    String endPointWithoutLeadingSlash = endpoint.replaceAll("^/", "");
    return new URL(baseWithoutTrailingSlash + "/" + endPointWithoutLeadingSlash);
  }
}

