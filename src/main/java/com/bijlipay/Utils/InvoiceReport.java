package com.bijlipay.Utils;


import com.bijlipay.Model.NewInvoice;
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

import static com.bijlipay.Utils.Constants.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;

public class InvoiceReport {

    private static final String NA = "NA";
//    DecimalFormat df = new DecimalFormat("00000000");

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
        int y = d.getMonth() < 5 ? d.getYear() - 1 : d.getYear();
        return y + "-" + (y + 1);
    }

    public void createPDF(NewInvoice response, String path) {

        try {
            response.setCategory("B2B");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getMerchant_GstrNumber());
            if (response.getMerchant_GstrNumber().equals(NA)) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>gst number");
                response.setCategory("B2C");
                response.setMerchant_GstrNumber("NA");
            }
            System.out.println("test1>>>>>>>>>>>");
            String pdfFilename = response.getName();
//            NtlmPasswordAuthentication auth =
//                    new NtlmPasswordAuthentication(SMB_PDF_DOMAIN, SMB_PDF_USERNAME, SMB_PDF_PASSWORD);
//            SmbFile file = new SmbFile(PDF_RENTAL_FILE_PATH_NEW + DateUtil.currentDate(MMM_yy) + "/" + pdfFilename + "_" + response.getTid() + ".pdf");
            File file = new File(PDF_RENTAL_FILE_PATH_NEW + DateUtil.currentDate(MMM_yy) + "/" + pdfFilename + "_" + response.getTid() + ".pdf");
//            SmbFileOutputStream fileOut = (SmbFileOutputStream) file.getOutputStream();
            FileOutputStream fileOut = new FileOutputStream(file);

            Document document = new Document();
            PdfWriter.getInstance(document, fileOut);

            // Inserting Image in PDF
            Image image = Image.getInstance(LOGO_IMAGE);
            image.scaleAbsolute(100f, 25f);
            Image signatureImage = Image.getInstance((SIGNATURE_IMAGE));
            NumberToWords convertToWords = new NumberToWords();
            Double value = Double.parseDouble(response.getCentralGst()) + Double.parseDouble(response.getStateGst()) + Double.parseDouble(response.getiGst())
                    + Double.parseDouble(response.getValue());

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
            System.out.println("test11>>>>>>>>>>>");
            table8.addCell(getSubHeaderCellwithNoBottomBorder( response.getDate().substring(response.getDate().indexOf("-") + 1, 6) + "-" + response.getInvoice_Number() + "/23-24" ));
            System.out.println("inside");
            table8.addCell(getBillRowCellwithNoLeftBorder("Invoiced Date: "));
            table8.addCell(getSubHeaderCellwithNoBottomBorder(DateUtil.dateForInvoice(response.getDate())));
            tax_invoice.addCell(table8);
            System.out.println("test2>>>>>>>>>>>");

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
            table.addCell(getBillRowCell(" "));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    4");
            table.addCell(getBillRowCellwithNoLeftBorder(" 1800 4200 235 "));
            table.addCell(getBillRowCell(" T: " + response.getContactNumber()));
            tax_invoice.addCell(table);
            System.out.println("test3>>>>>>>>>>>");

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

            PdfPTable table3 = new PdfPTable(5); // one page contains 15 records
            table3.setWidthPercentage(100);
            table3.setWidths(new float[]{1, 5, 2, 2, 2});
            table3.addCell(getBillRowCellwithBoldwithNoLeftBorder("No"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Description"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Value"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("GST"));
            table3.addCell(getBillRowCellwithBoldwithNoTopBorder("Total"));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    8");
            table3.addCell(getBillRowCellwithNoLeftBorder("1"));
            table3.addCell(getBillRowCellwithBold(response.getDescription()));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", Double.parseDouble(response.getValue()))));
            table3.addCell(getBillRowCellWithRightAlign(""));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", Double.parseDouble(response.getValue()))));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    9");
            table3.addCell(getBillRowCellwithNoLeftBorder(""));
            table3.addCell(getBillRowCellwithBold(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("")));
            table3.addCell(getBillRowCellWithRightAlign(""));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("")));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    10");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" CGST 9% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", Double.parseDouble(response.getCentralGst()))));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", Double.parseDouble(response.getCentralGst()))));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    11");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" SGST 9% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", Double.parseDouble(response.getStateGst()))));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", Double.parseDouble(response.getStateGst()))));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCell(" IGST 18% "));
            table3.addCell(getBillRowCell(""));
            table3.addCell(getBillRowCellWithRightAlign(String.format("%.2f", Double.parseDouble(response.getiGst()))));
            table3.addCell(getBillRowCellWithRightAlignwithBold(String.format("%.2f", Double.parseDouble(response.getiGst()))));
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>    12");
            table3.addCell(getBillRowCellwithNoLeftBorder(" "));
            table3.addCell(getBillRowCellwithTopBorder("Total Amount"));
            table3.addCell(getBillRowCellwithTopBorder(""));

            Double totalGst = Double.valueOf(response.getCentralGst()) + Double.valueOf(response.getStateGst()) + Double.valueOf(response.getiGst());
            table3.addCell(getBillRowCellwithBoldwithTopBoarder(String.format("%.2f", totalGst.floatValue())));
            Double totalValue = Double.valueOf(response.getCentralGst()) + Double.valueOf(response.getStateGst()) + Double.valueOf(response.getiGst())
                    + Double.parseDouble(response.getValue());
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

            PdfPTable table5 = new PdfPTable(5);
            table5.setWidthPercentage(100);
            table5.setWidths(new float[]{2, 5, 4, 3, 2});
            table5.addCell(getBillRowCellwithBoldwithNoLeftBorder("Sec No."));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Service Type"));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("Heading Number "));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder("SAC/HSN"));
            table5.addCell(getBillRowCellwithBoldwithNoTopBorder(" Tax "));
            table5.addCell(getBillRowCellwithNoLeftBorder(" Section 7"));
            table5.addCell(getBillRowCell(" Rental And Leasing Services"));
            table5.addCell(getBillRowCellWithRightAlign(" 9971"));
            table5.addCell(getBillRowCellWithRightAlign(" 997114"));
            table5.addCell(getBillRowCellWithRightAlign(String.format("%.2f", totalGst.floatValue())));
            table5.addCell(getBillRowCellwithNoLeftBorder(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCellwithNoLeftBorder(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
            table5.addCell(getBillRowCell(" "));
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
            System.out.println("Pdf created successfully for " + pdfFilename + "_" + response.getTid());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
