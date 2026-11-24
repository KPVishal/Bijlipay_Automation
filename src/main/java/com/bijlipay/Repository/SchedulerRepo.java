package com.bijlipay.Repository;

import com.bijlipay.Model.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SchedulerRepo extends JpaRepository<Scheduler, Integer> {

	@Query(value = "SELECT * FROM [auto_Scheduler]  WHERE username =?", nativeQuery = true)
	Scheduler findusername(String username);

	@Modifying
	@Transactional
	@Query(value = "UPDATE  [auto_Scheduler] SET is_active = 'false' WHERE username =? " +
			"AND expired_At < GetDate() ", nativeQuery = true)
	void findUserActive(String username);

}
