package com.example.stock.repository;

import com.example.stock.domain.Stock;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

  /**
   * Pessimistic Lock
   * 실제로 데이터에 lock 을 걸어서 정합성을 맞추는 방법
   * exclusive lock 을 걸게 되면 다른 트랜잭션에서는 lock 이 해제되기 전에 데이터를 가져갈 수 없게 됩니다.
   */
  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from Stock s where s.id = :id")
  Stock findByIdWithPessimisticLock(Long id);

  /**
   * Optimistic Lock
   * 실제로 lock 을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법.
   * 내가 읽은 버전에서 수정사항이 생겼을 시 application 에서 다시 읽은후에 작업을 수행해야 합니다.
   */
  @Lock(value = LockModeType.OPTIMISTIC)
  @Query("select s from Stock s where s.id = :id")
  Stock findByIdWithOptimisticLock(Long id);


}
