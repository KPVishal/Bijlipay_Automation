package com.bijlipay.Repository;

import com.bijlipay.Model.CurrentReport;
import org.springframework.data.repository.CrudRepository;

public interface PreviousReportRepository extends CrudRepository<CurrentReport, String> {

}
