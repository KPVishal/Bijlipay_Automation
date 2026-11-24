package com.bijlipay.ServiceImplementation;

import com.bijlipay.Model.DTO.SwitchTransactions;
import com.bijlipay.Repository.AxisRepository;
import com.bijlipay.Service.SwitchToTsdbService;
import com.bijlipay.Utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SwitchToTsdbServiceImp implements SwitchToTsdbService {

    private final Logger logger = LoggerFactory.getLogger(SwitchToTsdbServiceImp.class);
    @Autowired
    AxisRepository axisRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer insertToTransaction(String fetchTime) {
        return axisRepository.insertSwitchTransactionToDatabase(fetchTime);
    }

    @Override
    public Integer insertToTransactionCurrent() {

        return axisRepository.insertSwitchTransactionToDatabaseFull();
    }

    @Override
    public String fetchTransactionInitTime() {
        return axisRepository.fetchTransactionInitTime();
    }


    public void fetchAndSaveTxnData(List<Object[]> transactionDatas) {
        Date date = null;
        try {
            List<SwitchTransactions> list = new ArrayList<>();
            transactionDatas.forEach(o -> {
                SwitchTransactions transactions = new SwitchTransactions();
                transactions.setMid(o[2] == null ? "NA" : o[2].toString());
                transactions.setTxnType(o[12] == null ? "NA" : o[12].toString());
                transactions.setStan(o[6] == null ? "NA" : o[6].toString());
                transactions.setTid(o[7] == null ? "NA" : o[7].toString());
                transactions.setTxnTime(o[11] == null ? "NA" : o[11].toString());
                transactions.setTxnamount(o[8] == null ? 0 : Float.parseFloat(o[8].toString()));
                transactions.setTxnAdditionalAmount(o[13] == null ? Double.valueOf(0) : Double.valueOf(o[13].toString()));
                transactions.setBatchNumber(o[0] == null ? "NA" : o[0].toString());
                transactions.setInvoiceNumber(o[1] == null ? "NA" : o[1].toString());
                transactions.setrRNumber(o[4] == null ? "NA" : o[4].toString());
                transactions.setTxnResponseCode(o[10] == null ? "NA" : o[10].toString());
                transactions.setTxn_Type(null);
                transactions.setUrn(o[14] == null ? "NA" : o[14].toString());
                transactions.setMti(o[3] == null ? "NA" : o[3].toString());
                transactions.setSetlDate(date);
                transactions.setUploadDate(date);
                Date txnDateAndTime = o[5] == null ? new Date() : DateUtil.convertStringToDate(o[5].toString(), "yyyy-MM-dd hh:mm:ss");
                transactions.setResponseReceivedTime(txnDateAndTime);
                transactions.setSwitchTxnDate(o[9] == null ? "NA" : o[9].toString());
                list.add(transactions);
            });

            List<SwitchTransactions> switchTransactions = list;
            if (switchTransactions.size() > 0) {
                switchTransactions.forEach(s -> {
                    int update_pending = entityManager.createNativeQuery("insert into TSDB.dbo.bj_transaction"
                                    + "(account,AggregatorName,agrGrossAmount,agr_net_payment,agrRslrCgst,agrRslrDiscFees," +
                                    "agrRslrIgst,agrRslrInstv,agrRslrMdr,agrRslrMinFees,agrRslrSgst,agrRslrTds,Approval_Code," +
                                    "ASSOCIATION_fee,AssociationName,authCode,auth_id_rsp,bank,Batch_Code,card_category,CardNo," +
                                    "CardProduct,card_sub_category,cardType,CardType1,Card_type_A,cardTypeDesc,cc_fuel_new_ic_fee," +
                                    "cgst,city,createdAt,creditDebitFlag,email,gst,ifsc,igst,incentive,ins_code,instance," +
                                    "interchangeType,location,mcc,MCC_Desc,mdr_sharing_amount,mdrDiscFee,mdr_bj_calculation,mdr_perc_table," +
                                    "mdr_percentage,mer_payment,merchantMdr,merchantName,mid,min_util_fees,month,mdr,msg_type," +
                                    "txnType,Narration,ne_MDR,net_payment,Netamount,network,NEW_IC_Fee,new_non_ic_fee,onnus_offus," +
                                    "paymentId,paymentMode,payment_to,Paymentdate,posEntryMode,prj_serial_number,rRN,remarks," +
                                    "resp_code,setl_date,sgst,shell,source,stan,state,status,surcharge,surcharge_new_omc,switch_type,tds,TerminalSerialNo," +
                                    "tid,trace_no,Tradename,transactionCurrencyCode,transactionType,txn_count,txnDate,txn_mode,txn_sharing_amount," +
                                    "txnTime,txnamount,upload_date,txn_additional_amount,batch_number,invoice_number,rrn_number,response_received_time," +
                                    "txn_response_code,txn_type,urn,mti,switch_txn_date)" + " values " +
                                    "(:account,:aggregatorName,:agrGrossAmount,:agrNetPayment,:agrRslrCgst,:agrRslrDiscFee,:agrRslrIgst,:agrRslrInstv,:agrRslrMdr," +
                                    ":agrRslrMinFees,:agrRslrSgst,:agrRslrTds,:approvalCode,:associationFee,:associationName,:authCode," +
                                    ":authIdRsp,:bank,:batchCode,:cardCategory,:cardNo,:cardProduct,:cardSubCategory," +
                                    ":cardType,:cardType1,:cardTypeA,:cardTypeDesc,:ccFuelNewIcFee,:cgst,:city,CONVERT(datetime2,:createdAt)," +
                                    ":creditDebitFlag,:email,:gst,:ifsc,:igst,:incentive,:insCode,:instance," +
                                    ":interchangeType,:location,:mcc,:mccDesc,:mdrSharingAmount,:mdrDiscFee,:mdrBjCalculation," +
                                    ":mdrPercTable,:mdrPercentage,:mdrPayment,:merchantMdr,:merchantName,:mid,:minUtilFees,:month," +
                                    ":mdr,:msgType,:txnType,:narration,:neMdr,:netPayment,:netAmount,:network,:newIcFee,:newNonIcFee," +
                                    ":onnusOffus,:paymentId,:paymentMode,:paymentTo,CONVERT(datetime2,:paymentDate),:posEntryMode,:prjSerialNumber,:rrn," +
                                    ":remarks,:respCode,CONVERT(datetime2,:setlDate),:sgst,:shell,:source,:stan,:state,:status,:surcharge,:surcharge_new_omc," +
                                    ":switchType,:tds,:terminalSerialNumber,:tid,:traceNo,:traceName,:transactionCurrencyCode,:transactionType," +
                                    ":txnCount,CONVERT(datetime2,:txnDate),:txnMode,:txnSharingAmount,:txnTime,:txnAmount,CONVERT(datetime2,:uploadDate),:txnAdditionalAmount," +
                                    ":batchNumber,:invoiceNumber,:rrnNumber,CONVERT(datetime2,:responseReceivedTime),:txnResponseCode,:txnType,:urn," +
                                    ":mti,:switchTxnDate)")
                            .setParameter("account", s.getAccount())
                            .setParameter("aggregatorName", s.getAggregatorName())
                            .setParameter("agrGrossAmount", s.getAgrGrossAmount())
                            .setParameter("agrNetPayment", s.getAgrNetPayment())
                            .setParameter("agrRslrCgst", s.getAgrRslrCgst())
                            .setParameter("agrRslrCgst", s.getAgrRslrCgst())
                            .setParameter("agrRslrDiscFee", s.getAgrRslrDiscFees())
                            .setParameter("agrRslrIgst", s.getAgrRslrIgst())
                            .setParameter("agrRslrInstv", s.getAgrRslrInstv())
                            .setParameter("agrRslrMdr", s.getAgrRslrMdr())
                            .setParameter("agrRslrMinFees", s.getAgrRslrMinFees())
                            .setParameter("agrRslrSgst", s.getAgrRslrSgst())
                            .setParameter("agrRslrTds", s.getAgrRslrTds())
                            .setParameter("approvalCode", s.getApprovalCode())
                            .setParameter("associationFee", s.getAssocationFee())
                            .setParameter("associationName", s.getAssociationName())
                            .setParameter("authCode", s.getAuthCode())
                            .setParameter("authIdRsp", s.getAuthIdRsp())
                            .setParameter("bank", s.getBank())
                            .setParameter("batchCode", s.getBatchCode())
                            .setParameter("cardCategory", s.getCardCategory())
                            .setParameter("cardNo", s.getCardNo())
                            .setParameter("cardProduct", s.getCardProduct())
                            .setParameter("cardSubCategory", s.getCardSubCategory())
                            .setParameter("cardType", s.getCardType())
                            .setParameter("cardType1", s.getCardType1())
                            .setParameter("cardTypeA", s.getCardTypeA())
                            .setParameter("cardTypeDesc", s.getCardTypeDesc())
                            .setParameter("ccFuelNewIcFee", s.getCc_fuel_new_ic_fee())
                            .setParameter("cgst", s.getCgst())
                            .setParameter("city", s.getCity())
                            .setParameter("createdAt", null)
                            .setParameter("creditDebitFlag", s.getCreditDebitFlag())
                            .setParameter("email", s.getEmail())
                            .setParameter("gst", s.getGst())
                            .setParameter("ifsc", s.getIfsc())
                            .setParameter("igst", s.getIgst())
                            .setParameter("incentive", s.getIncentive())
                            .setParameter("insCode", s.getInsCode())
//                            .setParameter("insertedAt",s.getInsertedAt())
                            .setParameter("instance", s.getInstance())
                            .setParameter("interchangeType", s.getInterchangeType())
                            .setParameter("location", s.getLocation())
                            .setParameter("mcc", s.getMcc())
                            .setParameter("mccDesc", s.getMcc_Desc())
                            .setParameter("mdrSharingAmount", s.getMdrSharingAmount())
                            .setParameter("mdrDiscFee", s.getMdrDiscFee())
                            .setParameter("mdrBjCalculation", s.getMdr_bj_calculation())
                            .setParameter("mdrPercTable", s.getMdr_perc_table())
                            .setParameter("mdrPercentage", s.getMdr_percentage())
                            .setParameter("mdrPayment", s.getMerPayment())
                            .setParameter("merchantMdr", s.getMerchantMdr())
                            .setParameter("merchantName", s.getMerchantName())
                            .setParameter("mid", s.getMid())
                            .setParameter("minUtilFees", s.getMinUtilFees())
                            .setParameter("month", s.getMonth())
                            .setParameter("mdr", s.getMdrAmount())
                            .setParameter("msgType", s.getMsgType())
                            .setParameter("txnType", s.getTxnType())
                            .setParameter("narration", s.getNarration())
                            .setParameter("neMdr", s.getNe_MDR())
                            .setParameter("netPayment", s.getNetPayment())
                            .setParameter("netAmount", s.getNetamount())
                            .setParameter("network", s.getNetwork())
                            .setParameter("newIcFee", s.getNew_IC_Fee())
                            .setParameter("newNonIcFee", s.getNew_non_ic_fee())
                            .setParameter("onnusOffus", s.getOnnusOffus())
                            .setParameter("paymentId", s.getPaymentId())
                            .setParameter("paymentMode", s.getPaymentMode())
                            .setParameter("paymentTo", s.getPaymentTo())
                            .setParameter("paymentDate", s.getPaymentdate() == null ? null : DateUtil.convertDateToString(s.getPaymentdate(), "yyyy-MM-dd hh:mm:ss"))
                            .setParameter("posEntryMode", s.getPosEntryMode())
                            .setParameter("prjSerialNumber", s.getPrjSerialNumber())
                            .setParameter("rrn", s.getrRN())
                            .setParameter("remarks", s.getRemarks())
                            .setParameter("respCode", s.getRespCode())
                            .setParameter("setlDate", DateUtil.convertDateToString(s.getSetlDate(), "yyyy-MM-dd hh:mm:ss"))
                            .setParameter("sgst", s.getSgst())
                            .setParameter("shell", s.getShell())
                            .setParameter("source", s.getSorce())
                            .setParameter("stan", s.getStan())
                            .setParameter("state", s.getState())
                            .setParameter("status", s.getStatus())
                            .setParameter("surcharge", s.getSurcharge())
                            .setParameter("surcharge_new_omc", s.getSurchargeNewOmc())
                            .setParameter("switchType", s.getSwitchType())
                            .setParameter("tds", s.getTds())
                            .setParameter("terminalSerialNumber", s.getTerminalSerialNo())
                            .setParameter("tid", s.getTid())
                            .setParameter("traceNo", s.getTraceNo())
                            .setParameter("traceName", s.getTradename())
                            .setParameter("transactionCurrencyCode", s.getTransactionCurrencyCode())
                            .setParameter("transactionType", s.getTransactionType())
                            .setParameter("txnCount", s.getTxnCount())
                            .setParameter("txnDate", DateUtil.convertDateToString(s.getTxnDate(), "yyyy-MM-dd hh:mm:ss"))
                            .setParameter("txnMode", s.getTxnMode())
                            .setParameter("txnSharingAmount", s.getTxnSharingAmount())
                            .setParameter("txnTime", s.getTxnTime())
                            .setParameter("txnAmount", s.getTxnamount())
                            .setParameter("uploadDate", DateUtil.convertDateToString(s.getUploadDate(), "yyyy-MM-dd hh:mm:ss"))
                            .setParameter("txnAdditionalAmount", s.getTxnAdditionalAmount())
                            .setParameter("batchNumber", s.getBatchNumber())
                            .setParameter("invoiceNumber", s.getInvoiceNumber())
                            .setParameter("rrnNumber", s.getrRNumber())
                            .setParameter("responseReceivedTime", DateUtil.convertDateToString(s.getResponseReceivedTime(), "yyyy-MM-dd hh:mm:ss"))
                            .setParameter("txnResponseCode", s.getTxnResponseCode())
                            .setParameter("txnType", s.getTxnType())
                            .setParameter("urn", s.getUrn())
                            .setParameter("mti", s.getMti())
                            .setParameter("switchTxnDate", s.getSwitchTxnDate())
                            .executeUpdate();
                });
            }

        } catch (Exception e) {
            logger.info("Error on while inserting records {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
