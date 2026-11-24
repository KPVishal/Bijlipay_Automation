package com.bijlipay.Repository;

import com.bijlipay.Model.NewInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewInvoiceRepos extends JpaRepository<NewInvoice, Integer>{
	
	@Query(value="SELECT MAX(invoice_Number) FROM auto_New_Invoice", nativeQuery = true)
	int invoice_number();

	@Query(value="SELECT * FROM auto_New_Invoice WHERE created_at = ?1", nativeQuery = true)
	List<NewInvoice> getdata(String string);

}
