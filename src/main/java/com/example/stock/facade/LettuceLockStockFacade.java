package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

  private RedisLockRepository redisLockRepository;
  private StockService stockService;

  public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
    this.redisLockRepository = redisLockRepository;
    this.stockService = stockService;
  }

  public void decrease(Long key, Long quantity) throws InterruptedException {
    while (!redisLockRepository.lock(key)) { //lock 획득 시도 실패시
      Thread.sleep(100); //100millisecond 이후 시도
    }

    try { //lock 획득에 성공
      stockService.decrease(key, quantity);
    } finally {
      redisLockRepository.unlock(key); //lock 해제
    }
  }
}
