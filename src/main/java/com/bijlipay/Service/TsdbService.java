package com.bijlipay.Service;

import com.bijlipay.Model.DTO.AxisDTO;
import com.bijlipay.Model.DTO.KVBDTo;
import com.bijlipay.Model.DailySettlement;
import com.bijlipay.Model.FederalDTO;
import com.bijlipay.Model.MisTable;
import com.bijlipay.Model.SibReport;

import java.util.List;

public interface TsdbService {


    public void insertAxis(List<AxisDTO> misDTOS);
    public void insertFedral(List<FederalDTO> misDTOS);

    public void insertKvb(List<KVBDTo> misDTOS);

    public void insertSib(List<SibReport> misDTOS);

    public void insertCorp(List<MisTable> misDTOS);


    public void insertCorpSettlement(List<DailySettlement> misDTOS);

    public String fetchInitTime();

    String fetchInitTimeForQR();




}
