package com.bijlipay.Utils;


/*
 *
 * Create a constants for all instance
 *
 * */
public final class Constants {

    //TODO DB CONNECTION STRING
//    public static final String URL_Auth = "jdbc:sqlserver://10.20.20.49:1433;databaseName=Authentication";
//    public static final String URL = "jdbc:sqlserver://10.20.20.49:1433;databaseName=Bijlipay";
//    public static final String DBUSERNAME = "devuser";
//    public static final String DBPASSWORD = "Program@123";

    public static final String URL_Auth = "jdbc:sqlserver://10.8.0.20:1433;databaseName=Authentication";
    public static final String URL = "jdbc:sqlserver://10.8.0.20:1433;databaseName=Bijlipay";
    public static final String DBUSERNAME = "custrepuser";
    public static final String DBPASSWORD = "Custom@123$";


//    PROD PROPERTIES
    public static final String URLEARTH = "jdbc:sqlserver://10.8.0.20:1433;databaseName=TSDB";
    public static final String DBUSERNAMEEARTH = "custrepuser";
    public static final String DBPASSWORDEARTH = "Custom@123$";

//UAT PROPERTIES
//    public static final String URLEARTH = "jdbc:sqlserver://192.168.4.49:1433;databaseName=TSDB";
//    public static final String DBUSERNAMEEARTH = "devuser";
//    public static final String DBPASSWORDEARTH = "Program@123";


    public static final String URLMIRA = "jdbc:sqlserver://192.168.2.61:1433;databaseName=Transaction_Base";
    public static final String DBUSERNAMEMIRA = "custrepuser";
    public static final String DBPASSWORDMIRA = "Custom@123$";
    //    public static final String URL_Auth = "jdbc:sqlserver://localhost:1433;databaseName=Authentication";
//    public static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Bijlipay";
//    public static final String DBUSERNAME = "root";
//    public static final String DBPASSWORD = "Rapter@123";

   
    // TODO File Paths AND DIR's
    public static final String home = System.getProperty("user.home");

    public static final String LOCALFILEPATH_FEDERAL = home + "/Documents/federal/";
    public static final String LOCALFILEPATH_KVB = home + "/Documents/kvb/";
    public static final String LOCALFILEPATH_AXIS = home + "/Documents/axis/";
    //    public static final String LOCALFILEPATH_AXIS = "D:\\Sandhya\\axis\\";
    public static final String MERCHANT_FILE_DIRECTORY_FEDERAL = home + "/Documents/federal/excel/";
    public static final String LOCALFILEPATH_SIB = home + "/Documents/sib/";


    // TODO SFTP Conection Strings of MRL
    public static final String SFTP_HOST = "115.113.36.207";
    public static final String SFTP_USERNAME = "mrlbijilipay";

    public static final String SFTP_PASSWORD = "Tr@nsfer#2024";

//    public static final String SFTP_PASSWORD = "Atos@bijilipay123";
//    public static final String SFTP_PASSWORD = "Mrl@bijlipay@789";
    public static final int SFTP_PORT = 22;

    // TODO SMB Connection string of Mars input(FEDERAL)
    public static final String SMB_FEDERAL_DOMAIN = "workgroup";
    public static final String SMB_FEDERAL_USERNAME = "marsinput";
    public static final String SMB_FEDERAL_PASSWORD = "LBEuzGpKU23FggvO";
//Axis domain ,user,password
    public static final String SMB_AXIS_DOMAIN = "workgroup";
    public static final String SMB_AXIS_USERNAME = "empayapp";
    public static final String SMB_AXIS_PASSWORD = "GHJ^*&58988";
//Axis path
    //  public static final String MARS_FILE_PATH_AXIS = "smb://192.168.2.66/inputaxis/"; // Older file path
//    public static final String MARS_FILE_PATH_AXIS = "smb://10.10.0.72/axisinput/"; //New Older path
    public static final String MARS_FILE_PATH_AXIS = "/media/axisinput/"; //New Mount path

    public static final String MARS_FILE_PATH = "smb://192.168.2.61/INPUTFB/";
    public static final String MARS_FILE_PATH_KVB = "smb://192.168.2.61/inputkvb/";
    //    public static final String MARS_FILE_PATH = "C:\\Users\\vinothkumar\\Documents\\GitHub\\Bijlipay_Automation\\Automation\\Settlement\\INPUTFB\\";
//    public static final String MARS_FILE_PATH = "./Settlement/INPUTFB/";
//    public static final String MARS_FILE_PATH = "./Settlement/INPUTFB/";
//    public static final String MARS_FILE_PATH = "/media/titanshare/INPUTFB/";

    // TODO SMB Connection string of Settlement(PDF INVOICE)
    public static final String SMB_PDF_DOMAIN = "bijlipay";
    public static final String SMB_PDF_USERNAME = "invoice_reports";
    public static final String SMB_PDF_PASSWORD = "Invrep@555";
    public static final String PDF_FILE_PATH = "smb://192.168.2.18/Settlement/Rental/";
    //    public static final String PDF_RENTAL_FILE_PATH = "smb://192.168.2.18/Settlement/RentalInvoice/";
//    public static final String PDF_RENTAL_FILE_PATH_NEW = "C:\\Users\\vinothkumar\\Documents\\GitHub\\Bijlipay_Automation\\Automation\\Settlement\\RentalInvoice\\";
//    public static final String PDF_RENTAL_FILE_PATH_NEW = "./Settlement/RentalInvoice/";
    public static final String PDF_RENTAL_FILE_PATH_NEW = "/media/titanshare/rentalInvoice/";
    //    public static final String PDF_MDR_FILE_PATH = "smb://192.168.2.18/Settlement/MdrInvoice/";
//    public static final String PDF_MDR_FILE_PATH_NEW = "C:\\Users\\vinothkumar\\Documents\\GitHub\\Bijlipay_Automation\\Automation\\Settlement\\MdrInvoice\\";
//    public static final String PDF_MDR_FILE_PATH_NEW = "./Settlement/MdrInvoice/";
    public static final String PDF_MDR_FILE_PATH_NEW = "/media/titanshare/MdrInvoice/";

    public static final String MMM = "MMM";
    public static final String yyyy = "yyyy";
    public static final String MMddyyyy = "MMddyyyy";
    public static final String ddMMyyyy = "ddMMyyyy";
    public static final String MMM_yy = "MMM_yy";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DD_MM_YY = "dd-MM-yy";
    public static final String yyyy_MM_dd_HH_MM_SS_AA = "yyyy-MM-dd HH:MM:SS";
    public static final String DD_MM_YYYY_HH_MM_SS_AA = "dd/MM/yyyy hh:mm:ss aa";
    public static final String MM_DD_YYYY_HH_MM_SS_AA = "MM/dd/yyyy hh:mm:ss aa";
    public static final String MM_DD_YYYY_HH_MM_SS_AA_HITACHI = "dd/MM/yyyy hh:mm:ss";
    public static final String YYYYMMDDHHMMSS = "YYYYMMDDHHmmSS";
    public static final String dd_MMM_yyyy = "dd-MMM-yyyy";
    public static final String HH_mm_ss_SSS = "HH:mm:ss:SSS";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String HHmmss = "HHmmss";
    public static final String ddMMyy = "ddMMyy"; // 230720
    public static final String MMddyy = "MM/dd/yy";
    public static final String MMM_YY = "MMM_YY";
    public static final String MMM__YY = "MMM-YY";
    public static final String yyyyMMdd = "yyyy/MM/dd";
    public static final String YYYYMMDD = "dd_MM_yyyy";
    public static final String ddMM = "ddMM"; //2307
    public static final String MMMMyy = "mmmmyy"; //july20
    public static final String yy = "yy"; //july20
    //====================
    // TODO constant file name prefix
    public static final String BIJLIPAY_MIS_XLSX_FEDERAL = "00068_BIJLIPAY_MIS_REPORT_";
    public static final String BIJLIPAY_MIS_XLSX_AXIS = "Bijlipay_Aggr_Report_8_MMS_";
    public static final String BIJLIPAY_MIS_XLSX_KVB = "00009_KVB - BIJLIPAY_MIS_REPORT_";
    //==========
    //TODO SIB FILE PATHS
    public static final String BIJLIPAY_MIS_XLSX_SIB_ZIP = "SIB_BIJLIPAY_REPORTS_";
    public static final String BIJLIPAY_MIS_XLSX_SIB_FILENAME = "00074_SOUTH INDIAN BANK - BIJLIPAY_MIS_REPORT_";
//    public static final String SFTP_SIB_PATH = "/users/MRL-RECON/Bijlipay/SIB/";

    public static final String SFTP_SIB_PATH = "/Bijlipay/SIB/";
    public static final String MARS_SIB_FILE_PATH = "smb://192.168.2.61/inputsib/";
    public static final String MARS_SIB_FILENAME = "SIB_TXN_";



    public static final String BIJLIPAY_SETTLEMENT_REPORT = "00068_BIJLIPAY_SETTLEMENT_REPORT";
    //    ============
    public static final String FILENAME_KVBtoMARS = "00009_KVB - BIJLIPAY_MIS_REPORT_";

    public static final String FILENAME_FEDERAL = "FED_TXN_";
    //    public static final String FILENAME_FEDERAL = "FED_TXN_Testing";
//    public static final String FILENAME_AXIS = "AXIS_TXN_";
    public static final String FILENAME_AXIS = "Bijlipay_Aggr_Report_8_";
    //==================
//    public static final String[] CC_FEDERAL = {"sriram@bijlipay.co.in", "bhanuprathap@bijlipay.co.in"};
    public static final String[] SENTO_FEDERAL = {"bijli.ops@bijlipay.co.in", "recon@bijlipay.co.in"};

    public static final String[] SENTO_SIB = {"bijli.ops@bijlipay.co.in", "recon@bijlipay.co.in"};

    //    public static final String[] CC_AXIS = {"yugendran@bijlipay.co.in"};
//    public static final String[] CC_AXIS = {"sriram@bijlipay.co.in", "bhanuprathap@bijlipay.co.in"};
    public static final String[] SENTO_AXIS = {"bijli.ops@bijlipay.co.in", "recon@bijlipay.co.in"};

    public static final String[] SFTP_AXIS = {"bijli.ops@bijlipay.co.in", "recon@bijlipay.co.in","itadmin@bijlipay.co.in","bhanuprathap@bijlipay.co.in",
            "logamithran@bijlipay.co.in","ramalingom.sundaram@bijlipay.co.in","txn.support@bijlipay.co.in"};

    public static final String fromEmailId = "misreports@bijlipay.co.in";
    public static final String fromEmailId1 = "bijlipay.Invoice@bijlipay.co.in";
    // TODO Excel Headers Formats
    public static final String[] HEADERS_FEDERAL = {"Date Time of Txn", "MID", "TID", "Merchant Name", "City", "Card No", "Card Type", "RRN", "Association Name", "Approval Code", "Batch Code", "Txnamount", "Mrlmdr", "GST", "Netamount", "Paymentdate", "Aggregator Name", "Tradename", "TerminalSerialNo", "CardType1", "AssociationFee", "Narration", "Card Product"};
    //    public static final String[] HEADERS_AXIS = {"MID", "TID", "Merchant Name","Card No", "Transaction Type", "Transaction Amount","Transaction Date","Transaction Time","RRN", "Approval Code", "Interchange Type","Pos Entry Mode","Credit Debit Flag","Stan","Transaction Currency Code","Card Type","Card Type Desc"};
    public static final String[] HEADERS_AXIS = {"MERCHANT_ID", "TERMINAL_ID", "MERCHANT_NAME", "CARD_NUMBER", "TRANSACTION_TYPE", "TRANSACTION_AMOUNT", "TRANSACTION_DATE", "TRANSACTION_TIME", "RRN", "AUTH_ID", "INTERCHANGE_TYPE", "POS_ENTRY_MODE", "CREDIT_DEBIT_FLAG", "STAN", "TRANSACTION_CURRENCY_CODE", "CARD_TYPE", "CARD_TYPE_DESC"};
    public static final String[] SENTO_KvbtoMars = {"bijli.ops@bijlipay.co.in", "recon@bijlipay.co.in"};
    //  =================================================================================================================================================
    // TODO File Paths AND DIR's
    public static final String MERCHANT_FILE_DEATILS = home + "/Documents/Bijlipay_to Merchants.xlsx";

    public static final String MERCHANT_FILE_DEATILS_SwitchReport = home + "/Documents/Bijlipay_to Merchants_SwitchReport.xlsx";

    public static final String FILEDIRECTORY_MIS = home + "/Documents/mis/";
//    public static final String BASEURL = "/users/MRL-RECON/Bijlipay/";

//    public static final String FILEDIRECTORY_MIS = "/Bijlipay/";

    public static final String BASEURL = "/Bijlipay/";

    public static final String MERCHANT_FILE_DIRECTORY_MIS = home + "/Documents/mis/excel/";

    public static final String SCHEDULER = home + "/Documents/Schedule.xlsx";

    public static final String SFTPWORKINGDIR_MIS = home + "/Documents";

    // TODO Buffer size insert a value in a table
    public static final int BINARY_FILE_TYPE = 0;
    public static final int BUFFER_SIZE = 4096;

    // TODO FILE PATH
    public static final String UPLOAD_DOCUMENT_DIR_MIS = "uploadDir/";
    public static final String LOGO_IMAGE = "uploadDir/logo.png";
    public static final String SIGNATURE_IMAGE = "uploadDir/signature.jpg";
    public static final String UPLOAD_DOCUMENT_DIR_SCHEDULE_MIS = "uploadDir/Schedule.xlsx";
    public static final String UPLOAD_DOCUMENT_DIR_Bijlipay_to_Merchants_MIS = "uploadDir/Bijlipay_to Merchants.xlsx";
    //    public static final String FILE_NAME=
    // TODO SFTP Upload tomcat Conection Strings
    public static final String TOMCAT_SFTP_HOST_MIS = "192.168.2.79";
    public static final String TOMCAT_SFTP_USERNAME_MIS = "tomcat";
    public static final String TOMCAT_SFTP_PASSWORD_MIS = "F!nPort@12020";
    public static final int TOMCAT_SFTP_PORT_MIS = 22;

    // TODO Zip password for mis file
    public static final String ZIPFILEPASSWORD = "Bijli$pay12";

    // TODO FILE READING
    public static final String BIJLIPAY_MIS_XLSX__MIS = "CORPORATION BANK - BIJLIPAY_mis.xlsx";
    public static final String BIJLIPAY_SETTLEMENT_XLSX__MIS = "CORPORATION BANK - BIJLIPAY_Settlement.xlsx";
    public static final String PENDING_XLSX = "pending.xlsx";
    public static final String PAYMENT_FILE = "internalFile.xlsx";
    public static final String[] HEADERS_KVB = {"TRANSACTION DATE", "MID", "TID", "MERCHANT_NAME", "TRADENAME", "CITY", "CARDNO", "CARDTYPE", "RRN", "ASSOCIATION_NAME", "AUTH_CODE", "BATCH_CODE", "TXNAMOUNT", "MDRAMOUNT", "GSTAMOUNT", "NETAMOUNT", "PAYMENTDATE", "AGGREGATORNAME", "CARD_TYPE", "CARD_PRODUCT", "ASSOCIATION_COST", "TRANSACTION_TYPE"};
    // TODO Excel Headers Formats
    public static final String[] ONE_AND_SEVEN = {"Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No", "Card_Type"
            , "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "Paymentdate", "narration"};
    public static final String[] TWO = {"Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No", "Card_Type"
            , "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "location", "Paymentdate", "narration"};
    public static final String[] THREE = {"Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No", "Card_Type"
            , "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "MdrPercentage", "Paymentdate", "narration"};
    public static final String[] FOUR = {"Txn_Date", "MID", "TID", "Merchant_Name", "City", "State", "Card_No", "Card_Type"
            , "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "Paymentdate", "narration"};
    public static final String[] FIVE_AND_TEN = {"Txn_Date", "Txn_Time", "MID", "TID", "Merchant_Name", "City", "Card_No", "Card_Type"
            , "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "Paymentdate", "narration"};
    public static final String[] SIX = {"Txn_Date", "MID", "TID", "Merchant_Name", "City", "Card_No", "Card_Type", "RRN", "Association_Name",
            "Txnamount", "MDR", "GST", "Netamount", "CardType1", "Tradename", "Paymentdate", "narration", "incentive"};
    public static final String[] ELEVEN = {"MTI", "TxnType", "TerminalId", "MerchantId", "TxnDate", "TxnTime", "TxnAmount", "TxnResponseCode", "ResponseReceivedTime",
            "RRNumber", "Stan", "InvoiceNumber", "BatchNumber", "Urn"};



    //TODO COMMON FORMAT OF MAIl AND EXCEL
    public static final int FORMAT_0 = 0;
    public static final int FORMAT_1 = 1;
    public static final int FORMAT_2 = 2;
    public static final int FORMAT_3 = 3;
    public static final int FORMAT_4 = 4;
    public static final int FORMAT_5 = 5;
    public static final int FORMAT_6 = 6;
    public static final int FORMAT_7 = 7;
    public static final int FORMAT_10 = 10;
    public static final int FORMAT_11 = 11;


    public static final String INTERNALMAILDTO = "mohammad@bijlipay.co.in,pushparaj@bijlipay.co.in";
    public static final String[] SHELLMAILIDS = {"mohammad@bijlipay.co.in", "pushparaj@bijlipay.co.in"};
    public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    // TODO TABLES UPDATE AND INSERT
    public static final String INSERTINTOMTD = "Insert into Bijlipay_Mis_MTD$ SELECT Txn_Date ,MID , TID ,Merchant_Name " +
            ",City ,Card_No ,Card_Type ,Card_type_A ,RRN ,Association_Name, [Approval Code] ,[Batch Code] ,Txnamount  ,New_IC_Fee  ," +
            "mdr ,GST,Netamount ,Paymentdate ,Aggregator_Name ,Tradename ,Terminal_Serial_No ,Card_Type1 ," +
            "ASSOCIATION_fee ,Narration ,Bank ,Location ,State ,MCC_Desc ,MCC ,Ne_MDR ,mdr_percentage ,surcharge " +
            ",Card_Product,surcharge_new_omc ,new_non_ic_fee ,cc_fuel_new_ic_fee ,ifsc ,mdr_perc_table ,mdr_bj_calculation ,Incentive  FROM Bijlipay_Mis$";

    public static final String INSERTINTOMIS = "Insert into Bijlipay_Mis$(Txn_Date,MID,TID,Merchant_Name,City,Card_No ,Card_Type,Card_type_A,RRN,Association_Name,Approval_Code,Batch_Code,Txnamount,New_IC_Fee,mdr,\n" +
            "GST,Netamount,Paymentdate,Aggregator_Name,Tradename,Terminal_Serial_No,Card_Type1,ASSOCIATION_fee,Narration,Bank,Location,State ,\n" +
            "MCC_Desc ,MCC,Ne_MDR,mdr_percentage,surcharge,Card_Product,surcharge_new_omc,new_non_ic_fee,cc_fuel_new_ic_fee,ifsc,mdr_perc_table,mdr_bj_calculation,Incentive,email\n) " +
            "SELECT Txn_Date,MID,TID,Merchant_Name,City,Card_No ,Card_Type,Card_type_A,RRN,Association_Name,Approval_Code,Batch_Code,Txnamount,New_IC_Fee,mdr,\n" +
            "GST,Netamount,Paymentdate,Aggregator_Name,Tradename,Terminal_Serial_No,Card_Type1,ASSOCIATION_fee,Narration,Bank,Location,State ,\n" +
            "MCC_Desc ,MCC,Ne_MDR,mdr_percentage,surcharge,Card_Product,surcharge_new_omc,new_non_ic_fee,cc_fuel_new_ic_fee,ifsc,mdr_perc_table,mdr_bj_calculation,Incentive,email\n" +
            " FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] ";

 /*   public static final String INSERTINTOSETTLEMENT = "Insert into [TSDB].[dbo].[BijlipaySettlement]" +
            "(Txn_Date,MID,TID,Merchant_Name,City,ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount,Paymentdate,Typeidentifier,Upload_date)" +
            "SELECT Txn_Date,MID,TID,Merchant_Name,City,ifsc,Card_No,card_type,RRN,Association_Name,Txnamount,mdr,GST,Netamount,Paymentdate,'CORP',CONVERT (date, GETDATE())" +
            " FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] ";*/


    public static final String INSERTINTODAILYSETT = "Insert into daily_sett SELECT [account number]," +
            "associationfee," +
            "[branch code],card_type,cardproduct,city,date,gst,merchant_name,mid,mrlmdr,netamount,tid,txnamount,txncount " +
            "FROM [Authentication].[dbo].[auto_daily_sett]";

    public static final String[] MISUPDATE = {
            "UPDATE  m SET m.merchant_name  = f.Merchants_Establishment_Name ,m.state= f.Merchants_State ,m.bank = f" +
                    ".Terminals_Bank, m.ifsc = f.Terminals_Ifsc_Code, m.email = f.merchants_primary_email,m.MCC_Desc  = f.Terminals_mcc FROM " +
                    "[Authentication].[dbo].[auto_bijlipay_mis_temp$] m INNER JOIN ( SELECT Terminals_TID,Merchants_Establishment_Name," +
                    "Merchants_State,Terminals_Bank,Terminals_Ifsc_Code ,merchants_primary_email,Terminals_mcc FROM Bijlipay.dbo.GPRS_MID_MASTER$ )f" +
                    " ON m.TID = f.Terminals_TID",
            "UPDATE  m SET m.location  = f.location FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] m " +
                    "INNER JOIN  ( SELECT TID_no,location  FROM TBSE_LOCATION$ )f ON m.TID = f.tid_no",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET card_type_A =3  where Narration='Cash@pos'",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET card_type_A =2 where card_type='debit' and Narration='Pos'",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET card_type_A =1  where Narration='Pos' and card_type in ('dinners','credit')",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  mdr_percentage = ((MDR/txnamount) * 100) where txnamount>0",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  Txnamount = 0 where txnamount is null",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  MDR=0 where MDR is null",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  GST=0 where GST is null",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  netamount=0 where netamount is null",
            "UPDATE  m SET m.incentive  = f.merate FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] m INNER JOIN (SELECT [Terminals_TID]," +
                    "merate  FROM Bijlipay.dbo.GPRS_MID_MASTER$ )f ON m.narration='cash@pos' and m.TID = f" +
                    ".[Terminals_TID] and m.txnamount >=1000",
            "UPDATE  [Authentication].[dbo].[auto_bijlipay_mis_temp$] SET  incentive=0 where incentive is null"
    };

    public static final String DELETEMISTABLE = "TRUNCATE TABLE Bijlipay_Mis$";
    public static final String DELETEDAILYSETT = "TRUNCATE TABLE daily_sett";

    public static final String YES = "yes";
    //    =================================================================================================================
    // TODO Excel Headers Formats

    public static final String LEAD_DIRECTORY = home + "/Documents/";
    public static final String INVOICE_FILES_DIR = "/var/sftp/uploads";
    public static final String CUSTOMER_MAIL_DIR = INVOICE_FILES_DIR + "/customerMailIds.xlsx";
    public static final String[] UPDATEQUERY = {
            "UPDATE clix_MID_master$ set Total_transaction_Value =0 , Net_transaction_Value=0, Merchant_Share=0,Clix_Capital_Share=0,Settled_with_Clix_Capital=0,MDR =0,gst=0",
            "UPDATE clix_MID_master$ set count = datediff(day,VALUE_DATE, getdate() )",
            "UPDATE m SET m.Total_transaction_value  = f.txnamount FROM clix_MID_master$  m INNER JOIN (SELECT MID,SUM(txnamount)txnamount FROM clix_sett$ GROUP BY MID)f ON m.mid = f.mid",
            "UPDATE m SET m.Net_transaction_Value  = f.Netamount FROM clix_MID_master$  m INNER JOIN (SELECT mid,SUM(Netamount)Netamount FROM clix_sett$ GROUP BY  MID)f ON m.mid = f.mid",
            "UPDATE m SET m.MDR  = f.mrlMDR FROM clix_MID_master$ m INNER JOIN ( SELECT mid,SUM(mrlMDR) mrlMDR FROM clix_sett$ GROUP BY  MID )f ON m.mid = f.mid",
            "UPDATE m SET m.GST  = f.GST FROM clix_MID_master$ m INNER JOIN(SELECT mid,SUM(GST)GST FROM clix_sett$ GROUP BY  MID ) f ON m.mid = f.mid",
            "UPDATE m SET m.Clix_Capital_Share = (m.Clix_capital_Share1 * m.count) + f.Pending_Balance FROM clix_MID_master$  m INNER JOIN(SELECT MID, Pending_Balance  FROM Clix_MID_Pending$ )f ON m.mid = f.mid",
            "UPDATE clix_MID_master$ set VALUE_DATE = GETDATE()"};

    //DONWLOAD REPORTS
    public static final String[] FORMATONE_MIS = {"TID", "Net_Value", "Merchants_Establishment_Name", "Date"};
    public static final String[] FORMATTWO_MIS = {"TID", "Net_Value", "Date"};
    public static final String[] FORMATTHREE_MIS = {"Txn_Date", "MID", "TID", "Merchant_Name",
            "City", "Card_No", "Card_Type", "RRN", "Association_Name", "Txnamount", "MDR", "GST", "Netamount",
            "Card_Type1", "Tradename", "Paymentdate", "narration"};
    public static final String[] FORMATFOUR_MIS = {"TID", "Txn_amount", "Merchant_Name", "Paymentdate"};
    public static final String[] FORMATFIVE_MIS = {"Txn_Date", "MID", "TID", "Merchant_Name",
            "City", "Card_No", "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code",
            "Txnamount", "MDR", "GST", "Netamount",
            "Paymentdate", "Terminals_Mcc"};
    public static final String[] GPRS = {"Terminals_TID", "Terminals_MID",
            "Created_Date", "Legal_Name", "Terminals_Communication_Mode",
            "Terminals_Mcc", "Terminals_Bank", "IFSC", "BANKINFO_ACCOUNT_NO"};
    public static final String[] EMI = {"Txn_Date", "MID", "TID",
            "Merchant_Name", "City", "Card_No", "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code",
            "Txnamount", "MDR", "GST", "Netamount", "Paymentdate", "Aggregator_Name", "Tradename",
            "Terminal_Serial_No", "Card_Type1", "ASSOCIATION_fee", "Narration", "Card_Product",
            "BPRegion"};
    public static final String[] FLASH_REPORT = {"TID", "Card_Type", "Narration", "Nos", "Val"};
    public static final String[] DCB_SHEET1 = {"TID", "Txn_Count", "Txn_Amount"};
    public static final String[] FINANCE_SHEET = {"TID", "Txn_Count", "Txn_Amount", "MDR_Amount", "GST_Amount", "Net_Amount"};
    public static final String[] AMEXASS = {"Association_Name"};
    public static final String[] AMEXDATA = {"TID", "Paymentdate", "Txn_Amount", "Txn_Count"};
    public static final String[] INVOICE_MDR_HEADER = {"TID", "Terminals_MID",
            "Merchant_name", "address1", "address2", "Merchants_State", "Gstr_no", "Txn <= 2k",
            "Txn > 2k", "Txn <= 2k -GST", "Txn > 2k -GST", "Txn <= 2k -MDR", "Txn > 2k -MDR", "invoice_no"};
    public static final String[] ConsolidateCommisionDeatils = {"Terminal_ID", "Network", "Settle",
            "Txn_Count", "Txn_Amount", "MDR_Amount", "GST_Amount", "Net_Amount"};
    public static final String[] DC = {"Txn_Mode", "Txn_Count", "Txn_Rs", "Type"};
    public static final String[] RBI_DATA = {"Merchant_ID", "Terminal_ID", "Payment_Mode", "Payment_To", "Payment_ID", "Legal_Name", "City",
            "Card_Number", "MCC", "MCC_Description", "Transaction_Type", "Card_Sub_Category", "Card_Category", "Gross_Amount", "Merchant_MDR", "Merchant_Discount_Fees"
            , "SGST", "CGST", "IGST", "Incentive", "Service_Fee", "Min_Util_Fees", "Merchant_Net_Payment", "Aggregator_MDR", "Aggregator_Discount_Fees", "SGST1", "CGST1", "IGST1",
            "Incentive1", "Service_Fee1", "Min_Util_Fees1", "Net_Amount", "Aggregator_Commision", "Net_Payment", "Txn_Sharing_Amount", "MDR_Sharing_Amount", "MSG_Type", "RRN", "Response_Code", "Txn", "Settle", "Onus/Offus",
            "Card_Type", "Txn_Mode", "Network", "Remarks", "GST", "Identifier", "Pos_Entry_Mode",
            "Auth_Id_Rsp", "Trace_No", "IFSC"};
    public static final String[] RBI_SUMMARY = {"Txn_Count", "Txn_Amount"};
    public static final String[] Settlement_SUMMARY = {"SETTLEMENTDATE", "TXNCOUNT", "TXNAMOUNT"};
    public static final String[] Settlement_DATA = {"Txn_Date", "MID", "TID", "Merchant_Name", "City",
            "Card_No", "Card_Type", "RRN", "Association_Name", "Approval_Code", "Batch_Code", "Txnamount",
            "MDR", "GST", "Netamount", "Paymentdate"};


    public static final String INSERTINTOSETTLEMENT = "Insert into [TSDB].[dbo].[BijlipaySettlement]" +
            "(Txn_Date,MID,TID,Merchant_Name,City,ifsc,Card_No,Card_Type,RRN,Association_Name,Txnamount,MDR,GST,Netamount,Paymentdate,Typeidentifier,Upload_date)" +
            "SELECT Txn_Date,MID,TID,Merchant_Name,City,ifsc,Card_No,card_type,RRN,Association_Name,Txnamount,mdr,GST,Netamount,Paymentdate,'CORP',CONVERT (date, GETDATE())" +
            " FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] ";


    public static final String CorpDate = DateUtil.yesterday();

    public static final String CORPDATA =
            "INSERT INTO TSDB.dbo.BijlipaySettlement " +
                    "(Association_Name, Card_No, Card_Type, City, GST, MDR, Merchant_Name, MID, Netamount, Paymentdate, RRN, TID, Txnamount, Txn_Date, ifsc, Typeidentifier, Upload_date) " +
                    "SELECT Association_Name, Card_No, card_type, City, GST, mdr, Merchant_Name, MID, Netamount, Paymentdate, RRN, TID, Txnamount, Txn_Date, ifsc, 'CORP', CONVERT(date, GETDATE()) " +
                    "FROM [Authentication].[dbo].[auto_bijlipay_mis_temp$] ";

}
