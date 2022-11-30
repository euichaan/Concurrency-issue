package com.example.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

  @Autowired
  private PessimisticLockStockService stockService;

  @Autowired
  private StockRepository stockRepository;

  @BeforeEach //테스트 메서드 실행 전에 수행된다.
  public void before() {
    Stock stock = new Stock(1L, 100L);
    stockRepository.saveAndFlush(stock);
  }

  @AfterEach //테스트 메서드 실행 이후에 수행된다.
  public void after() {
    stockRepository.deleteAll();
  }

  @Test
  public void 재고감소() throws Exception {
    stockService.decrease(1L, 1L);
    // 100 - 1
    Stock stock = stockRepository.findById(1L).orElseThrow();
    assertEquals(99, stock.getQuantity());
  }

  @Test
  public void 동시에_100개의_요청 () throws Exception {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    // ExecutorService 는 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와주는 자바 API

    CountDownLatch latch = new CountDownLatch(threadCount);
    // 다른 스레드에서 수행 중인 작업이 완료될 때 까지 대기할 수 있도록 도와주는 클래스
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          stockService.decrease(1L, 1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    Stock stock = stockRepository.findById(1L).orElseThrow();
    // 100 - 1 * 100
    assertEquals(0L, stock.getQuantity());
  }

}