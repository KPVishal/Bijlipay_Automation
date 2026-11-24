package com.bijlipay.Repository;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.SibReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface SIBRepository extends JpaRepository<SibReport,Long> {
    int countBydate(String oneDayBefore);
}
