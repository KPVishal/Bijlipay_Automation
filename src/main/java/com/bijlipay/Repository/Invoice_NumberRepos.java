package com.bijlipay.Repository;

import com.bijlipay.Model.Invoice_Number;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface Invoice_NumberRepos extends CrudRepository<Invoice_Number, Long> {

	@Query(value = "SELECT * FROM [auto_Invoice_Number]", nativeQuery = true)
	Invoice_Number findbyInvoiceId();

	@Transactional
	@Modifying
	@Query(value = "UPDATE [auto_Invoice_Number] SET invoice_Number=?1 ", nativeQuery = true)
	void updateInvoiceNumber(long l);
}
