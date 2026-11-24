package com.bijlipay.Repository;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.FederalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FederalRepository extends JpaRepository<FederalDTO,Long> {
    int countBydate(String oneDayBefore);
}
