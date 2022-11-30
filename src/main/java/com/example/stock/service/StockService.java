package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

  private StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  /**
   * 매번 새로운 트랜잭션을 실행한다. 새로운 트랜잭션 안에서 예외가 발생해도 호출한 곳에는 롤백이 전파되지 않는다.
   * 즉, 2개의 트랜잭션은 완전히 독립적인 별개로 단위로 작동한다.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public synchronized void decrease(Long id, Long quantity) {
    // get stock
    // 재고감소
    // 저장
    Stock stock = stockRepository.findById(id).orElseThrow();

    stock.decrease(quantity);

    stockRepository.saveAndFlush(stock);
  }

}
