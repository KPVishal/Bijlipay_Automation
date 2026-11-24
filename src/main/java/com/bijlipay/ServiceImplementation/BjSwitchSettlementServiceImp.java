package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.BjSwitchSettlementDto;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Service.BjSwitchSettlementService;
import com.bijlipay.Utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BjSwitchSettlementServiceImp implements BjSwitchSettlementService {


    Long count = Long.valueOf(0);

    private static final Logger log = LoggerFactory.getLogger(BjSwitchSettlementServiceImp.class);
    private EntityManager entityManager;


    @Autowired
    AxisRepository axisRepository;


    @Override
    public Long fetchAndSaveTxnData(List<Object[]> transactionDatas) throws SQLException {
        try {
            System.out.println(transactionDatas.size());

            log.info("QUERY : START : ---- : {}", DateUtil.currentDate("DD-MM-YYYYY HH:MM:SS"));
//            List<BjSwitchSettlementDto> list = new ArrayList<>();
            transactionDatas.forEach(o -> {
                BjSwitchSettlementDto transactions = new BjSwitchSettlementDto();
                transactions.setId(Long.valueOf(o[0] == null ? "NA" : o[0].toString()));
                transactions.setInsCode(Long.parseLong(o[1] == null ? "NA" : o[1].toString()));
                transactions.setPrjSerialNumber(Long.parseLong(o[2] == null ? "NA" : o[2].toString()));
                transactions.setMid(o[3] == null ? "NA" : o[3].toString());
                transactions.setTid(o[4] == null ? "NA" : o[4].toString());
                transactions.setPaymentMode(o[5] == null ? "NA" : o[5].toString());
                transactions.setPaymentTo(o[6] == null ? "NA" : o[6].toString());
                transactions.setPaymentId(o[7] == null ? "NA" : o[7].toString());
                transactions.setMerchantName(o[8] == null ? "NA" : o[8].toString());
                transactions.setCity(o[9] == null ? "NA" : o[9].toString());
                transactions.setCardNo(o[10] == null ? "NA" : o[10].toString());
                transactions.setMcc(o[11] == null ? "NA" : o[11].toString());
                transactions.setMcc_Desc(o[12] == null ? "NA" : o[12].toString());
                transactions.setTransactionType(o[13] == null ? "NA" : o[13].toString());
                transactions.setCardSubCategory(o[14] == null ? "NA" : o[14].toString());
                transactions.setCardCategory(o[15] == null ? "NA" : o[15].toString());
                transactions.setAgrNetPayment(o[16] == null ? 0 : ((BigDecimal) o[16]).floatValue());
                transactions.setAgrGrossAmount(o[17] == null ? 0 : ((BigDecimal) o[17]).floatValue());
                transactions.setMerchantMdr(o[18] == null ? 0 : ((BigDecimal) o[18]).floatValue());
                transactions.setMdrDiscFee(o[19] == null ? 0 : ((BigDecimal) o[19]).floatValue());
                transactions.setSgst(o[20] == null ? 0 : ((BigDecimal) o[20]).floatValue());
                transactions.setCgst(o[21] == null ? 0 : ((BigDecimal) o[21]).floatValue());
                transactions.setIgst(o[22] == null ? 0 : ((BigDecimal) o[22]).floatValue());
                transactions.setIncentive(o[23] == null ? 0 : ((BigDecimal) o[23]).floatValue());
                transactions.setTds(o[24] == null ? 0 : ((BigDecimal) o[24]).longValue());
                transactions.setMinUtilFees(o[25] == null ? 0 : ((BigDecimal) o[25]).longValue());
                transactions.setMerPayment(o[26] == null ? 0 : ((BigDecimal) o[26]).floatValue());
                transactions.setAgrRslrMdr(o[27] == null ? 0 : ((BigDecimal) o[27]).floatValue());
                transactions.setAgrRslrDiscFees(o[28] == null ? 0 : ((BigDecimal) o[28]).floatValue());
                transactions.setAgrRslrSgst(o[29] == null ? 0 : ((BigDecimal) o[29]).floatValue());
                transactions.setAgrRslrCgst(o[30] == null ? 0 : ((BigDecimal) o[30]).floatValue());
                transactions.setAgrRslrIgst(o[31] == null ? 0 : ((BigDecimal) o[31]).floatValue());
                transactions.setAgrRslrInstv(o[32] == null ? 0 : ((BigDecimal) o[32]).floatValue());
                transactions.setAgrRslrTds(o[33] == null ? 0 : ((BigDecimal) o[33]).floatValue());
                transactions.setAgrRslrMinFees(o[34] == null ? 0 : ((BigDecimal) o[34]).floatValue());
                transactions.setNetPayment(o[35] == null ? 0 : ((BigDecimal) o[35]).floatValue());
                transactions.setTxnSharingAmount(o[36] == null ? 0 : ((BigDecimal) o[36]).floatValue());
                transactions.setMdrSharingAmount(o[37] == null ? 0 : ((BigDecimal) o[37]).floatValue());
                transactions.setMsgType(o[38] == null ? "NA" : o[38].toString());
                transactions.setTraceNo(o[39] == null ? "NA" : o[39].toString());
                transactions.setrRN(o[40] == null ? "NA" : o[40].toString());
                transactions.setRespCode(o[41] == null ? "NA" : o[41].toString());
                transactions.setAuthIdRsp(o[42] == null ? "NA" : o[42].toString());
                Date txnDateAndTime3 = o[43] == null ? new Date() : (Date) o[43];
                transactions.setTxnDate(txnDateAndTime3);
                transactions.setPosEntryMode(o[44] == null ? "NA" : o[44].toString());
                Date setlDate= o[45] == null ? new Date() : (Date) o[45];
                transactions.setSetlDate(setlDate);
                Date uploadDate= o[46] == null ? new Date() : (Date) o[46];
                transactions.setUploadDate(uploadDate);
                transactions.setOnnusOffus(o[47] == null ? "NA" : o[47].toString());
                transactions.setCardType(o[48] == null ? "NA" : o[48].toString());
                transactions.setTxnMode(o[49] == null ? "NA" : o[49].toString());
                transactions.setNetwork(o[50] == null ? "NA" : o[50].toString());
                transactions.setRemarks(o[51] == null ? "NA" : o[51].toString());
                transactions.setCardProduct(o[52] == null ? "NA" : o[52].toString());
                transactions.setIfsc(o[53] == null ? "NA" : o[53].toString());
                transactions.setAccount(o[54] == null ? "NA" : o[54].toString());
                transactions.setInstance(o[55] == null ? "NA" : o[55].toString());

                if(o[55] == "Axis"||o[55] =="Federal"||o[55]=="SIB"){
                    transactions.setSwitchType("non_bj");
                }else{
                    transactions.setSwitchType("bj");
                }
//                transactions.setBijlipaySwitch(true);
                log.info("Inserting Record - {}", transactions);
//                axisRepository.insertDataIntoBjSwitchSettlement(transactions);
                count++;
            });
            log.info("QUERY : END : ---- : {}", DateUtil.currentDate("DD-MM-YYYYY HH:MM:SS"));

        } catch (Exception e) {
            log.info("Error on while inserting records {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
           return count;
    }
}
