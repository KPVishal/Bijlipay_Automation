package com.bijlipay.Service;

import com.bijlipay.Model.DTO.FrmDataDTO;

import java.util.List;

public interface FrmDataService {
    List<FrmDataDTO> findAllFrmData();

    void updateFrmData();

    void deleteAllFrmData();

    void insertNewFrmData();
}
