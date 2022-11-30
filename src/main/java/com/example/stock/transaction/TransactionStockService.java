package com.example.stock.transaction;
import com.example.stock.service.StockService;

public class TransactionStockService {

  private StockService stockService;

  public TransactionStockService(StockService stockService) {
    this.stockService = stockService;
  }

  public void decrease(Long id, Long quantity) {
    startTransaction();

    stockService.decrease(id, quantity); // 10:00
    // 10:00 ~ 10:05 다른 스레드가 decrease 메서드를 호출할 수 있습니다.
    endTransaction(); // 10:05

  }
  public void startTransaction() {

  }

  public void endTransaction() {

  }

}
