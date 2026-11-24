package com.bijlipay.Repository;

import com.bijlipay.Model.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReportLogRepository extends JpaRepository<ReportLog, Integer> {

	@Query(value="SELECT new map(l.username as username,count(l) AS count ) FROM [auto_report_logs] l  GROUP BY l.username ",nativeQuery=true)
	List<ReportLog> findAllUserByOrder();



	@Query(value="SELECT l FROM ReportLog l WHERE l.username=?1  AND l.download_at >=?2 AND l.download_at <=?3 ")
	List<ReportLog> downloadReport(String username, String from, String to);

}
