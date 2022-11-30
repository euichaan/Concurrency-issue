package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {

  private OptimisticLockStockService optimisticLockStockService;

  public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
    this.optimisticLockStockService = optimisticLockStockService;
  }

  public void decrease(Long id, Long quantity) throws InterruptedException {
   while (true) {
     try {
       optimisticLockStockService.decrease(id, quantity);

       break; // 정상적으로 업데이트 된다면 update 후 break
     } catch (Exception e) {
        Thread.sleep(50); //수량 감소에 실패하면 50 millisecond 후에 재시도
     }
   }
  }
}
