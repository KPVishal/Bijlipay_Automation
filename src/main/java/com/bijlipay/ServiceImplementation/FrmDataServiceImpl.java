package com.bijlipay.ServiceImplementation;


import com.bijlipay.Model.DTO.FrmDataDTO;
import com.bijlipay.Service.FrmDataService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrmDataServiceImpl implements FrmDataService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FrmDataDTO> findAllFrmData() {
        String query = "SELECT [EventType],[EventSubType],[event-name] ,[msgBody],[host_id],[sys_time],[event_id],[channel],[user_id],[user_type],[cust_id]," +
                "[device_id],[ip_address],[ip_country],[ip_city],[cust_name],[succ_fail_flg],[error_code],[error_desc],[dr_account_id],[txn_amt],[avl_bal]," +
                "[cr_account_id],[cr_ifsc_code],[payee_id],[payee_name],[tran_type],[entry_mode],[branch_id],[acct_ownership],[acct_open_date],[part_tran_type]," +
                "[country_code],[cust_card_id],[bin],[mcc_code],[terminal_id],[merchant_id],[cust_mob_no],[txn_secured_flag],[product_code],[dev_owner_id]," +
                "[atm_dom_limit],[atm_intr_limit],[pos_ecom_dom_limit],[pos_ecom_intr_limit],[tran_date],[mask_card_no],[state_code],[card_type],[tran_time]," +
                "[pos_entry_mode],[interbankcard],[decline_type] FROM IOB.[dbo].[FRM_Data$]";

        Query q =
                entityManager.createNativeQuery(query);
        List<Object[]> results = (List<Object[]>) q.getResultList();
        List<FrmDataDTO> frmDataDTOS = new ArrayList<>();
        results.forEach(r -> {
            FrmDataDTO dataDTO = new FrmDataDTO();
            dataDTO.setEventType(r[0] == null || r[0].toString().trim().isEmpty() ? " " : r[0].toString());
            dataDTO.setEventSubType(r[1] == null || r[1].toString().trim().isEmpty() ? " " : r[1].toString());
            dataDTO.setEventName(r[2] == null || r[2].toString().trim().isEmpty() ? " " : r[2].toString());
            dataDTO.setMsgBody(r[3] == null || r[3].toString().trim().isEmpty() ? " " : r[3].toString());
            dataDTO.setHost_id(r[4] == null || r[4].toString().trim().isEmpty() ? " " : r[4].toString());
            dataDTO.setSys_time(r[5] == null || r[5].toString().trim().isEmpty() ? " " : r[5].toString());
            dataDTO.setEvent_id(r[6] == null || r[6].toString().trim().isEmpty() ? " " : r[6].toString());
            dataDTO.setChannel(r[7] == null || r[7].toString().trim().isEmpty() ? " " : r[7].toString());
            dataDTO.setUser_id(r[8] == null || r[8].toString().trim().isEmpty() ? " " : r[8].toString());
            dataDTO.setUser_type(r[9] == null || r[9].toString().trim().isEmpty() ? " " : r[9].toString());
            dataDTO.setCust_id(r[10] == null || r[10].toString().trim().isEmpty() ? " " : r[10].toString());
            dataDTO.setDevice_id(r[11] == null || r[11].toString().trim().isEmpty() ? " " : r[11].toString());
            dataDTO.setIp_address(r[12] == null || r[12].toString().trim().isEmpty() ? " " : r[12].toString());
            dataDTO.setIp_country(r[13] == null || r[13].toString().trim().isEmpty() ? " " : r[13].toString());
            dataDTO.setIp_city(r[14] == null || r[14].toString().trim().isEmpty() ? " " : r[14].toString());
            dataDTO.setCust_name(r[15] == null || r[15].toString().trim().isEmpty() ? " " : r[15].toString());
            dataDTO.setSucc_fail_flg(r[16] == null || r[16].toString().trim().isEmpty() ? " " : r[16].toString());
            dataDTO.setError_code(r[17] == null || r[17].toString().trim().isEmpty() ? " " : r[17].toString());
            dataDTO.setError_desc(r[18] == null || r[18].toString().trim().isEmpty() ? " " : r[18].toString());
            dataDTO.setDr_account_id(r[19] == null || r[19].toString().trim().isEmpty() ? " " : r[19].toString());
            dataDTO.setTxn_amt(r[20] == null || r[20].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[20]).floatValue());
            dataDTO.setAvl_bal(r[21] == null || r[21].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[21]).floatValue());
            dataDTO.setCr_account_id(r[22] == null || r[22].toString().trim().isEmpty() ? " " : r[22].toString());
            dataDTO.setCr_ifsc_code(r[23] == null || r[23].toString().trim().isEmpty() ? " " : r[23].toString());
            dataDTO.setPayee_id(r[24] == null || r[24].toString().trim().isEmpty() ? " " : r[24].toString());
            dataDTO.setPayee_name(r[25] == null || r[25].toString().trim().isEmpty() ? " " : r[25].toString());
            dataDTO.setTran_type(r[26] == null || r[26].toString().trim().isEmpty() ? " " : r[26].toString());
            dataDTO.setEntry_mode(r[27] == null || r[27].toString().trim().isEmpty() ? " " : r[27].toString());
            dataDTO.setBranch_id(r[28] == null || r[28].toString().trim().isEmpty() ? " " : r[28].toString());
            dataDTO.setAcct_ownership(r[29] == null || r[29].toString().trim().isEmpty() ? " " : r[29].toString());
            dataDTO.setAcct_open_date(r[30] == null || r[30].toString().trim().isEmpty() ? " " : r[30].toString());
            dataDTO.setPart_tran_type(r[31] == null || r[31].toString().trim().isEmpty() ? " " : r[31].toString());
            dataDTO.setCountry_code(r[32] == null || r[32].toString().trim().isEmpty() ? " " : r[32].toString());
            dataDTO.setCust_card_id(r[33] == null || r[33].toString().trim().isEmpty() ? " " : r[33].toString());
            dataDTO.setBin(r[34] == null || r[34].toString().trim().isEmpty() ? " " : r[34].toString());
            dataDTO.setMcc_code(r[35] == null || r[35].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[35]).floatValue());
            dataDTO.setTerminal_id(r[36] == null || r[36].toString().trim().isEmpty() ? " " : r[36].toString());
            dataDTO.setMerchant_id(r[37] == null || r[37].toString().trim().isEmpty() ? " " : r[37].toString());
            dataDTO.setCust_mob_no(r[38] == null || r[38].toString().trim().isEmpty() ? " " : r[38].toString());
            dataDTO.setTxn_secured_flag(r[39] == null || r[39].toString().trim().isEmpty() ? " " : r[39].toString());
            dataDTO.setProduct_code(r[40] == null || r[40].toString().trim().isEmpty() ? " " : r[40].toString());
            dataDTO.setDev_owner_id(r[41] == null || r[41].toString().trim().isEmpty() ? " " : r[41].toString());
            dataDTO.setAtm_dom_limit(r[42] == null || r[42].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[42]).floatValue());
            dataDTO.setAtm_intr_limit(r[43] == null || r[43].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[43]).floatValue());
            dataDTO.setPos_ecom_dom_limit(r[44] == null || r[44].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[44]).floatValue());
            dataDTO.setPos_ecom_intr_limit(r[45] == null || r[45].toString().trim().isEmpty() ? Float.valueOf(" ") : ((Double) r[45]).floatValue());
            dataDTO.setTran_date(r[46] == null || r[46].toString().trim().isEmpty() ? " " : r[46].toString());
            dataDTO.setMask_card_no(r[47] == null || r[47].toString().trim().isEmpty() ? " " : r[47].toString());
            dataDTO.setState_code(r[48] == null || r[48].toString().trim().isEmpty() ? " " : r[48].toString());
            dataDTO.setCard_type(r[49] == null || r[49].toString().trim().isEmpty() ? " " : r[49].toString());
            dataDTO.setTran_time(r[50] == null || r[50].toString().trim().isEmpty() ? " " : r[50].toString());
            dataDTO.setPos_entry_mode(r[51] == null || r[51].toString().trim().isEmpty() ? " " : r[51].toString());
            dataDTO.setInterbankcard(r[52] == null || r[52].toString().trim().isEmpty() ? " " : r[52].toString());
            dataDTO.setDecline_type(r[53] == null || r[53].toString().trim().isEmpty() ? " " : r[53].toString());
            frmDataDTOS.add(dataDTO);
        });

        return frmDataDTOS;
    }

    @Override
    public void updateFrmData() {
        entityManager.getTransaction().begin();

        String[] update = {"UPDATE  IOB.[dbo].[FRM_Data$] set EventType ='ft',EventSubType='cardtxn',[event-name] ='ft_cardtxn',host_id='F',channel='POS',user_type='RETAIL',succ_fail_flg ='F',country_code='IN',pos_ecom_dom_limit=999999,pos_ecom_intr_limit=999999,decline_type ='C'",
                "UPDATE   m SET m.Terminals_Mcc  = f.mcc  FROM Bijlipay.DBO.GPRS_MID_MASTER$ m INNER JOIN (SELECT mcc_desc,mcc FROM  Bijlipay.DBO.MCC_detail$ )f ON m.Terminals_Mcc = f.mcc_desc",
                "UPDATE   m SET m.device_id  = f.Terminals_Terminal_Serial_Number , m.mcc_code = f.mcc_code ,m.state_code = f.Merchants_State FROM IOB.[dbo].[FRM_Data$] m INNER JOIN (SELECT " +
                        "Terminals_TID,Terminals_Terminal_Serial_Number,mcc_code,Merchants_State FROM Bijlipay.DBO.GPRS_MID_MASTER$ )f ON m.terminal_id = f.Terminals_TID",
                "UPDATE IOB.[dbo].[FRM_Data$] set msgBody='',user_id ='',cust_id  ='',ip_address ='',ip_country ='',ip_city =''," +
                        "cust_name ='',error_code ='',dr_account_id ='',avl_bal ='',cr_account_id ='',cr_ifsc_code " +
                        "='',payee_id ='',payee_name ='',tran_type ='',entry_mode ='',branch_id ='',acct_ownership ='',acct_open_date ='',part_tran_type ='',cust_mob_no ='',txn_secured_flag ='',product_code ='',dev_owner_id ='',atm_dom_limit ='',atm_intr_limit ='',card_type ='',pos_entry_mode ='',interbankcard =''",
                "UPDATE   m SET m.interbankcard  = 'N' FROM IOB.[dbo].[FRM_Data$] m INNER JOIN ( SELECT IOBBIN FROM  IOBBIN$  )" +
                        "f ON m.bin = f.IOBBIN",
                "UPDATE   IOB.[dbo].[FRM_Data$] set  interbankcard  = 'Y'  WHERE interbankcard !='N'",
                "UPDATE IOB.[dbo].[FRM_Data$] set txn_secured_flag ='S'"};
        for (String updateQuery : update) {
            Query query = entityManager.createNativeQuery(updateQuery);
            int rowsUpdated = query.executeUpdate();
            System.out.println("entities Updated: " + rowsUpdated);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
    }

    @Override
    public void deleteAllFrmData() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery("DELETE FROM IOB.[dbo].[FRM_Data$]");
        int rowsDeleted = query.executeUpdate();
        System.out.println("entities deleted: " + rowsDeleted);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void insertNewFrmData() {
        String insertInto = "INSERT INTO IOB.[dbo].[FRM_Data$](sys_time,event_id ,error_desc,txn_amt,cust_card_id,bin," +
                "terminal_id,merchant_id,tran_date,mask_card_no,tran_time)SELECT TransactionDate , RRN, ProcessingCode, TxnAmount, PANNumber , left(pannumber,6)bin, tid, MID , TransactionDate, pannumber, TransactionDate  FROM Decline_Txn$ ";
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery(insertInto);
        int rowsInserted = query.executeUpdate();
        System.out.println("Entities Inserted: " + rowsInserted);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
