package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Stock, Long> {

  /**
   * Named Lock
   * 이름을 가진 lock을 획득한 후 해제할때까지 다른 세션은 이 lock을 획득할 수 없도록 합니다.
   * transaction이 종료될 때 lock이 자동으로 해제되지 않습니다. 별도의 명령어로 해제를 수행하거나
   * 선점시간이 끝나야 해제됩니다.
   */

  @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
  void getLock(String key);

  @Query(value = "select release_lock(:key)", nativeQuery = true)
  void releaseLock(String key);
}
