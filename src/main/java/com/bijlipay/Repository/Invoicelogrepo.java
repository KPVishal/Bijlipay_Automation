package com.bijlipay.Repository;

import com.bijlipay.Model.Invoice_Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Invoicelogrepo extends JpaRepository<Invoice_Log, Integer> {
	
	@Query(value="SELECT Top 1 * FROM [dbo].[auto_invoice_logs]  WHERE tid=?1 AND month=?2", nativeQuery = true)
	Invoice_Log tidswithdate(String tid, String longToDate);
	
	

}
