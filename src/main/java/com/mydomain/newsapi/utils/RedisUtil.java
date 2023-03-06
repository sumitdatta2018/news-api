package com.mydomain.newsapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RedisUtil {
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private static final String KEY_FORMATTER = "%s_%s";


  public <T> T getData(String keyholder, String key, Class<T> t) {
    if (keyholder == null || key == null)
      throw new IllegalArgumentException("keyholder and key parameter is required");
    ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
    Object rawData = opsForValue.get(keyMaker(keyholder, key));
    return objectMapper.convertValue(rawData, t);
  }

  public void saveData(String keyholder, String key, Object value, Integer timeout, TimeUnit unit) {
    if (keyholder == null || key == null || value == null)
      throw new IllegalArgumentException("keyholder,key and value parameter is required");
    String formedKey = keyMaker(keyholder, key);
    var opsForValue = redisTemplate.opsForValue();
    if (timeout != null && unit != null) {
      opsForValue.set(formedKey, value, timeout, unit);
    } else {
      opsForValue.set(formedKey, value);
    }
  }


  public void saveData(String keyholder, String key, Object value) {
    saveData(keyholder, key, value, null, null);
  }

  public void deleteData(String keyholder, String key) {
    if (keyholder == null || key == null)
      throw new IllegalArgumentException("keyholder and key parameter is required");
    redisTemplate.delete(keyMaker(keyholder, key));
  }

  public String keyMaker(String keyholder, String key) {
    return String.format(KEY_FORMATTER, keyholder, key);

  }

}
