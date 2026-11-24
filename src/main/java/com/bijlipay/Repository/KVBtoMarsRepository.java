package com.bijlipay.Repository;
import com.bijlipay.Model.DTO.KVBDTo;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KVBtoMarsRepository extends JpaRepository<KVBDTo,Long> {
    int countBydate(String oneDayBefore);
}
