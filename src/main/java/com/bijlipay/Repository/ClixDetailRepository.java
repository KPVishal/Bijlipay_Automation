package com.bijlipay.Repository;

import com.bijlipay.Model.ClixDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClixDetailRepository extends JpaRepository<ClixDetail,Integer> {
    boolean existsByMid(String midArrayresult);
}
