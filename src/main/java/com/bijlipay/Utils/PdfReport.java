package com.bijlipay.Utils;

import com.bijlipay.Model.Merchant_details;
import com.bijlipay.Repository.MerchantRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import static com.bijlipay.Utils.Constants.*;

public class PdfReport {

    private static final String NA = "NA";

    DecimalFormat df = new DecimalFormat("00000");

    @Autowired
    private MerchantRepository merchantRepository;

    private static PdfPCell getBillRowCellWithRightAlignAndTopBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellWithRightAlignwithBold(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithTopBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithNoBottomBorder(Image signatureImage) {
        signatureImage.scaleToFit(100, 100);
        PdfPCell cell = new PdfPCell(signatureImage);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithNoTopBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBoldAndUrderLine(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBoldwithNoBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBoldwithNoLeftBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBold(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBoldwithNoTopBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getBillRowCellwithBoldwithTopBoarder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(8.0f);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        return cell;
    }

    private static PdfPCell getSubHeaderCellwithLeftAlign(String text) {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getSubHeaderCellwithNoBottomBorder(String text) {
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(subFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        return cell;
    }

    private static PdfPCell getSubHeaderCellwithLeftAlignwithRightBorder(String text) {
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(subFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    private static PdfPCell getSubHeaderCell(String text) {
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(subFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        return cell;
    }

    private static PdfPCell getHeaderCell(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        return cell;

    }

    public static PdfPCell getBillRowCell(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    public static PdfPCell getBillRowCellWithRightAlign(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    public static PdfPCell getBillRowCellwithNoLeftBorder(String text) {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        FontSelector fs = new FontSelector();
        fs.addFont(catFont);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }

    public static String getFinancialYear() {
        Date d = new Date();
        int y = d.getMonth() < 3 ? d.getYear() - 1 : d.getYear();
        return y + "-" + (y + 1);
    }

    public static String getMrdmonth(long date) {
    	   
    	Date mDate = new Date(date * 1000L);
    	String[] m= {"JANM","FEBM","MARM","APRM","MAYM","JUNM","JULM","AUGM","SEPM","OCTM","NOVM","DECM"};
    	return m[mDate.getMonth()];
    }
    public void createPDF(long date, Merchant_details response, long updateVale) {
        try {
            response.setCategory("B2B");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getMerchant_GstrNumber());
            if (response.getMerchant_GstrNumber().equals(NA)) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>gst number");
                response.setCategory("B2C");
                response.setMerchant_GstrNumber("NA");
            }
            Double commonGst = response.getGreatergst();
            if (response.getStateCode() == 33) {
                Double ctrlGst = commonGst / 2;
                double valueRoundedctrlGst = Math.round(ctrlGst * 100D) / 100D;
                Double strlGst = commonGst / 2;
                double valueRoundedstrlGst = Math.round(strlGst * 100D) / 100D;
                Double iGst = 0.00;
                response.setStateGst(valueRoundedctrlGst);
                response.setCentralGst(valueRoundedstrlGst);
                response.setiGst(iGst);
            } else {
                response.setStateGst(0.00);
                response.setCentralGst(0.00);
                double valueRoundedIgst = Math.round(commonGst * 100D) / 100D;
                response.setiGst(valueRoundedIgst);
            }
            System.out.println("test1>>>>>>>>>>>");
            long update = updateVale + 1;
            String pdfFilename = response.getName();
//            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
//            SmbFile insert = new SmbFile(PDF_MDR_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy) + "/" + pdfFilename + "_" + response.getTid() + ".pdf", auth);
            File insert = new File(PDF_MDR_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy) + "/" + pdfFilename + "_" + response.getTid() + ".pdf");
//            SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
            FileOutputStream fileOut = new FileOutputStream(insert.getAbsolutePath());
            Document document = new Document();
            PdfWriter.getInstance(document, fileOut);
            Image image = Image.getInstance(LOGO_IMAGE);
            image.scaleAbsolute(100f, 25f);
            Image signatureImage = Image.getInstance((SIGNATURE_IMAGE));
            NumberToWords convertToWords = new NumberToWords();
            Double value = response.getCentralGst() + response.getStateGst() + response.getiGst()
                    + response.getGreatermdr() + response.getMdr();
            int total_amount = value.intValue(); // Total Amount
            int paise = (int) (value * 100) % 100;
            // Inserting Invoice Table
            PdfPTable tax_invoice = new PdfPTable(1);
            tax_invoice.setWidthPercentage(100);
            tax_invoice.setWidths(new float[]{1});
            tax_invoice.setSpacingBefore(5);
            tax_invoice.addCell(getHeaderCell("Tax Invoice"));

            PdfPTable table8 = new PdfPTable(4);
            tax_invoice.setWidthPercentage(100);
            table8.setWidths(new float[]{1, 3, 1, 1});
            table8.addCell(getBillRowCellwithNoLeftBorder("Invoice no:"));
            String finanicalYear = response.getDateForInvoice().substring(0, 3);
            System.out.println("finanacial year======== {} "+ finanicalYear);
            table8.addCell(getSubHeaderCellwithNoBottomBorder("STPL/" + response.getDateForInvoice() + "-" + df.format(updateVale)));
            System.out.println("inside" + updateVale);
            table8.addCell(getBillRowCellwithNoLeftBorder("Invoiced Date: "));
            table8.addCell(getSubHeaderCellwithNoBottomBorder(response.getDate() + "-" + response.getDateinvoice()));
            tax_invoice.addCell(table8);

            PdfPTable table = new PdfPTable(2); // one page contains 15 records
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 1});
            table.addCell(getSubHeaderCellwithLeftAlignwithRightBorder(" "));
            table.addCell(getSubHeaderCellwithLeftAlign("Invoiced to "));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    1");
            table.addCell(getBillRowCellwithBoldwithNoBorder(" SKILWORTH TECHNOLOGIES PVT. LTD"));
            table.addCell(getBillRowCellwithBold(" " + response.getName() + " " + "(" + response.getTid() + ")"));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    2");
            table.addCell(getBillRowCellwithNoLeftBorder(" #51, Chaitanya “Exotica” "));
            table.addCell(getBillRowCell(" " + response.getFirstAddress()));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    3");
            table.addCell(getBillRowCellwithNoLeftBorder(" 9th Floor, Venkatnarayana Road, T.Nagar, CH -17 "));
            table.addCell(getBillRowCell(" " + response.getSecondAddress()));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    4");
            table.addCell(getBillRowCellwithNoLeftBorder(" T: +91-44-46059614,46059608 "));
            table.addCell(getBillRowCell(" T: " + response.getContactNumber()));
            tax_invoice.addCell(table);

            PdfPTable table2 = new PdfPTable(4);
            table2.setWidthPercentage(100);
            table2.setWidths(new float[]{1, 2, 1, 2});

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    5");
            table2.addCell(getBillRowCellwithNoLeftBorder(" GSTR No: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(" 33AAKCS6703M1Z2 "));
            table2.addCell(getBillRowCell(" GSTR No: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getMerchant_GstrNumber())));

            table2.addCell(getBillRowCellwithNoLeftBorder(" State Code: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(" 33"));
            table2.addCell(getBillRowCell(" State Code: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getStateCode())));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    6");
            table2.addCell(getBillRowCellwithNoLeftBorder(" CIN: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(" U72900TN2007PTC075663"));
            table2.addCell(getBillRowCell(" Category:"));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getCategory())));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    7");
            table2.addCell(getBillRowCellwithNoLeftBorder(" PAN: "));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(" AAKCS6703M  "));
            table2.addCell(getBillRowCell(""));
            table2.addCell(getBillRowCellwithBoldwithNoBorder(""));
            tax_invoice.addCell(table2);

            PdfPTable table3 = new PdfPTable(6); // one page contains 15 records
            table3.setWidthPercentage(100);
            table3.setWidths(new float[]{1, 5, 2, 2, 2, 2});
            table3.addCell(getBillRowCellwithBoldwithNoLeftBorder("No"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Description"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Transaction Value "));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("MDR"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("GST"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Total"));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    8");
            table3.addCell(getBillRowCellwithNoLeftBorder("1"));
            table3.addCell(getBillRowCellwithBold("MDR on Transaction < 2000 INR"));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getTransaction_value().floatValue())));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getMdr().floatValue())));
            table3.addCell(getBillRowCellWithRightAlign(""));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getMdr().floatValue())));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    9");
            table3.addCell(getBillRowCellwithNoLeftBorder("2"));
            table3.addCell(getBillRowCellwithBold("MDR on Transaction > 2000 INR"));
            table3.addCell(
                    getBillRowCellWithRightAlign(String.format("%.2f", response.getGreatertransaction_value().floatValue())));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getGreatermdr().floatValue())));
            table3.addCell(getBillRowCellWithRightAlign(""));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getGreatermdr().floatValue())));


            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    10");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" CGST 9% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getCentralGst().floatValue())));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getCentralGst().floatValue())));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    11");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" SGST 9% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getStateGst().floatValue())));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getStateGst().floatValue())));

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" IGST 18% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getiGst().floatValue())));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getiGst().floatValue())));


            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");

            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCellwithTopBorder("Total Amount"));
            table3.addCell(getBillRowCellwithTopBorder(""));
            table3.addCell(getBillRowCellwithTopBorder(""));
            Double totalGst = response.getCentralGst() + response.getStateGst() + response.getiGst();
            table3.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalGst.floatValue())));
            Double totalValue = response.getCentralGst() + +response.getStateGst() + response.getiGst()
                    + response.getGreatermdr() + response.getMdr();
            table3.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalValue.floatValue())));
            tax_invoice.addCell(table3);

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    13");
            PdfPTable table4 = new PdfPTable(2);
            table4.setWidthPercentage(100);
            table4.setWidths(new float[]{2, 5});
            table4.addCell(getBillRowCellwithNoLeftBorder("Total Amount (in Words): "));
            table4.addCell(getSubHeaderCellwithNoBottomBorder("(INR) " + convertToWords.convertNumberToWords(total_amount)
                    + " Rupees and " + convertToWords.convertToPaise(paise) + " Paise Only"));
            tax_invoice.addCell(table4);

            PdfPTable table5 = new PdfPTable(6);
            table5.setWidthPercentage(100);
            table5.setWidths(new float[]{2, 5, 4, 3, 3, 2});
            table5.addCell(getBillRowCellwithBoldwithNoLeftBorder("Sec No."));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Service Type"));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Heading Number "));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Group Number"));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("SAC/HSN"));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder(" Tax "));

            table5.addCell(getBillRowCellwithNoLeftBorder(" Section 8"));
            table5.addCell(getBillRowCell(" Other Support Services"));
            table5.addCell(getBillRowCellWithRightAlign(" 9985"));
            table5.addCell(getBillRowCellWithRightAlign(" 99859"));
            table5.addCell(getBillRowCellWithRightAlign(" 998599"));
            table5.addCell(getBillRowCellWithRightAlign(String.format("%.2f", totalGst.floatValue())));

            table5.addCell(getBillRowCellwithNoLeftBorder(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));

            table5.addCell(getBillRowCellwithNoLeftBorder(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));


            table5.addCell(getBillRowCellwithNoLeftBorder(" "));
            table5.addCell(getBillRowCellwithTopBorder(" Total "));
            table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
            table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
            table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
            table5.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalGst.floatValue())));

            tax_invoice.addCell(table5);
            PdfPTable table6 = new PdfPTable(2); // one page contains 15 records
            table6.setWidthPercentage(100);
            table6.setWidths(new float[]{2, 1});

            table6.addCell(getBillRowCellwithNoLeftBorder(""));
            table6.addCell(getBillRowCellwithBoldAndUrderLine("For Skilworth Technologies Pvt Ltd"));

            table6.addCell(getBillRowCellwithNoLeftBorder(" "));
            table6.addCell(getBillRowCellwithNoBottomBorder(signatureImage));

            table6.addCell(getBillRowCellwithNoLeftBorder(" All disputes subject to CHENNAI jurisdiction only  "));
            table6.addCell(getBillRowCellwithNoTopBorder(" Authorised Signatory "));
            tax_invoice.addCell(table6);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell("This is a computer generated invoice, no signature required"));

            document.open();
            document.add(image);
            document.add(tax_invoice);
            document.add(describer);

            document.close();
            fileOut.close();
            System.out.println("Pdf created successfully for " + pdfFilename + "_" + response.getTid());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void createPDFMDR(long date, Merchant_details response, long updateVale) {
    try {
        response.setCategory("B2B");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getMerchant_GstrNumber());
        if (response.getMerchant_GstrNumber().equals(NA)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>gst number");
            response.setCategory("B2C");
            response.setMerchant_GstrNumber("NA");
        }
        Double commonGst = response.getGreatergst();
        if (response.getStateCode() == 33) {
            Double ctrlGst = commonGst / 2;
            double valueRoundedctrlGst = Math.round(ctrlGst * 100D) / 100D;
            Double strlGst = commonGst / 2;
            double valueRoundedstrlGst = Math.round(strlGst * 100D) / 100D;
            Double iGst = 0.00;
            response.setStateGst(valueRoundedctrlGst);
            response.setCentralGst(valueRoundedstrlGst);
            response.setiGst(iGst);
        } else {
            response.setStateGst(0.00);
            response.setCentralGst(0.00);
            double valueRoundedIgst = Math.round(commonGst * 100D) / 100D;
            response.setiGst(valueRoundedIgst);
        }
        System.out.println("test1>>>>>>>>>>>");
        long update = updateVale + 1;
        String pdfFilename = response.getName();
//        NtlmPasswordAuthentication auth =
//                new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
//        SmbFile insert =
//                new SmbFile(PDF_MDR_FILE_PATH + DateUtil.longToDate(date, MMM_yy) + "/" + pdfFilename + "_" + response.getTid() +
//                        ".pdf", auth);
               File insert =
                new File(PDF_MDR_FILE_PATH_NEW + DateUtil.longToDate(date, MMM_yy) + "/" + pdfFilename + "_" + response.getTid() +
                        ".pdf");
        FileOutputStream fileOut = new FileOutputStream(insert.getAbsolutePath());
//        SmbFileOutputStream fileOut = (SmbFileOutputStream) insert.getOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, fileOut);
        Image image = Image.getInstance(LOGO_IMAGE);
        image.scaleAbsolute(100f, 25f);
        Image signatureImage = Image.getInstance((SIGNATURE_IMAGE));
        NumberToWords convertToWords = new NumberToWords();
        Double value = response.getCentralGst() + response.getStateGst() + response.getiGst()
                + response.getGreatermdr() + response.getMdr();
        int total_amount = value.intValue(); // Total Amount
        int paise = (int) (value * 100) % 100;
        // Inserting Invoice Table
        PdfPTable tax_invoice = new PdfPTable(1);
        tax_invoice.setWidthPercentage(100);
        tax_invoice.setWidths(new float[]{1});
        tax_invoice.setSpacingBefore(5);
        tax_invoice.addCell(getHeaderCell("Tax Invoice"));

        PdfPTable table8 = new PdfPTable(4);
        tax_invoice.setWidthPercentage(100);
        table8.setWidths(new float[]{1, 3, 1, 1});
        table8.addCell(getBillRowCellwithNoLeftBorder("Invoice no:"));
        String finanicalYear = response.getDateForInvoice().substring(0, 3);
        System.out.println("financial year ---- {}"+finanicalYear);
        table8.addCell(getSubHeaderCellwithNoBottomBorder(getMrdmonth(date) + "-" + df.format(updateVale)));
        System.out.println("inside" + updateVale);
        table8.addCell(getBillRowCellwithNoLeftBorder("Invoiced Date: "));
        table8.addCell(getSubHeaderCellwithNoBottomBorder(response.getDate() + "-" + response.getDateinvoice()));
        tax_invoice.addCell(table8);

        PdfPTable table = new PdfPTable(2); // one page contains 15 records
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 1});
        table.addCell(getSubHeaderCellwithLeftAlignwithRightBorder(" "));
        table.addCell(getSubHeaderCellwithLeftAlign("Invoiced to "));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    1");
        table.addCell(getBillRowCellwithBoldwithNoBorder(" SKILWORTH TECHNOLOGIES PVT. LTD"));
        table.addCell(getBillRowCellwithBold(" " + response.getName() + " " + "(" + response.getTid() + ")"));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    2");
        table.addCell(getBillRowCellwithNoLeftBorder(" #51, Chaitanya “Exotica” "));
        table.addCell(getBillRowCell(" " + response.getFirstAddress()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    3");
        table.addCell(getBillRowCellwithNoLeftBorder(" 9th Floor, Venkatnarayana Road, T.Nagar, CH -17 "));
        table.addCell(getBillRowCell(" " + response.getSecondAddress()));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    4");
        table.addCell(getBillRowCellwithNoLeftBorder(" T: +91-44-46059614,46059608 "));
        table.addCell(getBillRowCell(" T: " + response.getContactNumber()));
        tax_invoice.addCell(table);

        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100);
        table2.setWidths(new float[]{1, 2, 1, 2});

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    5");
        table2.addCell(getBillRowCellwithNoLeftBorder(" GSTR No: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(" 33AAKCS6703M1Z2 "));
        table2.addCell(getBillRowCell(" GSTR No: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getMerchant_GstrNumber())));

        table2.addCell(getBillRowCellwithNoLeftBorder(" State Code: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(" 33"));
        table2.addCell(getBillRowCell(" State Code: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getStateCode())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    6");
        table2.addCell(getBillRowCellwithNoLeftBorder(" CIN: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(" U72900TN2007PTC075663"));
        table2.addCell(getBillRowCell(" Category:"));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(String.valueOf(response.getCategory())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    7");
        table2.addCell(getBillRowCellwithNoLeftBorder(" PAN: "));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(" AAKCS6703M  "));
        table2.addCell(getBillRowCell(""));
        table2.addCell(getBillRowCellwithBoldwithNoBorder(""));
        tax_invoice.addCell(table2);

        PdfPTable table3 = new PdfPTable(6); // one page contains 15 records
        table3.setWidthPercentage(100);
        table3.setWidths(new float[]{1, 5, 2, 2, 2, 2});
        table3.addCell(getBillRowCellwithBoldwithNoLeftBorder("No"));
        table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Description"));
        table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Transaction Value "));
        table3.addCell(getBillRowCellwithBoldwithNoTopBorder("MDR"));
        table3.addCell(getBillRowCellwithBoldwithNoTopBorder("GST"));
        table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Total"));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    8");
        table3.addCell(getBillRowCellwithNoLeftBorder("1"));
        table3.addCell(getBillRowCellwithBold("MDR on Transaction < 2000 INR"));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getTransaction_value().floatValue())));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getMdr().floatValue())));
        table3.addCell(getBillRowCellWithRightAlign(""));
        table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getMdr().floatValue())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    9");
        table3.addCell(getBillRowCellwithNoLeftBorder("2"));
        table3.addCell(getBillRowCellwithBold("MDR on Transaction > 2000 INR"));
        table3.addCell(
                getBillRowCellWithRightAlign(String.format("%.2f", response.getGreatertransaction_value().floatValue())));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getGreatermdr().floatValue())));
        table3.addCell(getBillRowCellWithRightAlign(""));
        table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getGreatermdr().floatValue())));


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    10");
        table3.addCell(getBillRowCellwithNoLeftBorder(" "));
        table3.addCell(getBillRowCell(" CGST 9% "));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getCentralGst().floatValue())));
        table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getCentralGst().floatValue())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    11");
        table3.addCell(getBillRowCellwithNoLeftBorder(" "));
        table3.addCell(getBillRowCell(" SGST 9% "));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getStateGst().floatValue())));
        table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getStateGst().floatValue())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");
        table3.addCell(getBillRowCellwithNoLeftBorder(" "));
        table3.addCell(getBillRowCell(" IGST 18% "));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCell(""));
        table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", response.getiGst().floatValue())));
        table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", response.getiGst().floatValue())));


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");

        table3.addCell(getBillRowCellwithNoLeftBorder(" "));
        table3.addCell(getBillRowCellwithTopBorder("Total Amount"));
        table3.addCell(getBillRowCellwithTopBorder(""));
        table3.addCell(getBillRowCellwithTopBorder(""));
        Double totalGst = response.getCentralGst() + response.getStateGst() + response.getiGst();
        table3.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalGst.floatValue())));
        Double totalValue = response.getCentralGst() + +response.getStateGst() + response.getiGst()
                + response.getGreatermdr() + response.getMdr();
        table3.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalValue.floatValue())));
        tax_invoice.addCell(table3);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    13");
        PdfPTable table4 = new PdfPTable(2);
        table4.setWidthPercentage(100);
        table4.setWidths(new float[]{2, 5});
        table4.addCell(getBillRowCellwithNoLeftBorder("Total Amount (in Words): "));
        table4.addCell(getSubHeaderCellwithNoBottomBorder("(INR) " + convertToWords.convertNumberToWords(total_amount)
                + " Rupees and " + convertToWords.convertToPaise(paise) + " Paise Only"));
        tax_invoice.addCell(table4);

        PdfPTable table5 = new PdfPTable(6);
        table5.setWidthPercentage(100);
        table5.setWidths(new float[]{2, 5, 4, 3, 3, 2});
        table5.addCell(getBillRowCellwithBoldwithNoLeftBorder("Sec No."));
        table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Service Type"));
        table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Heading Number "));
        table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Group Number"));
        table5.addCell(getBillRowCellwithBoldwithNoTopBorder("SAC/HSN"));
        table5.addCell(getBillRowCellwithBoldwithNoTopBorder(" Tax "));

        table5.addCell(getBillRowCellwithNoLeftBorder(" Section 8"));
        table5.addCell(getBillRowCell(" Other Support Services"));
        table5.addCell(getBillRowCellWithRightAlign(" 9985"));
        table5.addCell(getBillRowCellWithRightAlign(" 99859"));
        table5.addCell(getBillRowCellWithRightAlign(" 998599"));
        table5.addCell(getBillRowCellWithRightAlign(String.format("%.2f", totalGst.floatValue())));

        table5.addCell(getBillRowCellwithNoLeftBorder(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));

        table5.addCell(getBillRowCellwithNoLeftBorder(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));
        table5.addCell(getBillRowCell(" "));


        table5.addCell(getBillRowCellwithNoLeftBorder(" "));
        table5.addCell(getBillRowCellwithTopBorder(" Total "));
        table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
        table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
        table5.addCell(getBillRowCellwithBoldwithTopBoarder(" "));
        table5.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalGst.floatValue())));

        tax_invoice.addCell(table5);
        PdfPTable table6 = new PdfPTable(2); // one page contains 15 records
        table6.setWidthPercentage(100);
        table6.setWidths(new float[]{2, 1});

        table6.addCell(getBillRowCellwithNoLeftBorder(""));
        table6.addCell(getBillRowCellwithBoldAndUrderLine("For Skilworth Technologies Pvt Ltd"));

        table6.addCell(getBillRowCellwithNoLeftBorder(" "));
        table6.addCell(getBillRowCellwithNoBottomBorder(signatureImage));

        table6.addCell(getBillRowCellwithNoLeftBorder(" All disputes subject to CHENNAI jurisdiction only  "));
        table6.addCell(getBillRowCellwithNoTopBorder(" Authorised Signatory "));
        tax_invoice.addCell(table6);

        PdfPTable describer = new PdfPTable(1);
        describer.setWidthPercentage(100);
        describer.addCell(getdescCell("This is a computer generated invoice, no signature required"));

        document.open();
        document.add(image);
        document.add(tax_invoice);
        document.add(describer);

        document.close();
        fileOut.close();
        System.out.println("Pdf created successfully for " + pdfFilename + "_" + response.getTid());

    } catch (Exception e) {
        e.printStackTrace();
    	}
	}
   
}
