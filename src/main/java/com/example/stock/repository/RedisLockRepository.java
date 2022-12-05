package com.example.stock.repository;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisLockRepository {
  private RedisTemplate<String, String> redisTemplate;

  public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  //==key를 활용하여 setnx 명령어를 통한 lock==//
  public Boolean lock(Long key) {
    return redisTemplate
        .opsForValue()
        .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000)); //key 는 stock의 id, value는 lock이라는 문자
  }

  //==lock 해제==//
  public Boolean unlock(Long key) {
    return redisTemplate.delete(generateKey(key));
  }

  private String generateKey(Long key) {
    return key.toString();
  }

}
