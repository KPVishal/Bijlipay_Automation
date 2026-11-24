package com.bijlipay.Utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String currentDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        String currentDate = formatter.format(date);
        return currentDate;
    }

    public static String yesterday(){
        SimpleDateFormat formater = new SimpleDateFormat("MMdd");
        Date date = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        date = cal.getTime();
        String yes = formater.format(date);
        return yes;
    }

    public static String longToDate(long longDate, String pattern) {
        Date date = new Date(longDate * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateConvertToString = formatter.format(date);
        return dateConvertToString;
    }

    public static String calToDate(Date longDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(longDate);
    }

    public static Date longtoDate(long convert) {
        Date date = new Date(convert * 1000);
        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd");
        Date rdate = null;
        try {
            rdate = dateToString.parse(dateToString.format(date));
            System.out.println("rdate "+rdate);
        } catch (ParseException e) {e.printStackTrace();}
        return rdate;
    }

//=============================================================================================================================

    public static Date convertStringToDate(String stringCellValue, String format) {
        if (!(stringCellValue == "")) {
            SimpleDateFormat before = new SimpleDateFormat(format);
            Date date = null;
            try {
                date = before.parse(stringCellValue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return null;
        }
    }

    public static String convertDateToString(Date cDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentDate = "";
        if (cDate != null) {
            currentDate = formatter.format(cDate);
        }
        return currentDate;
    }


    public static LocalTime convertStringToTime(String stringCellValue) {

        if (!(stringCellValue == "")) {

            LocalTime t = LocalTime.parse("HH:mm:ss");
            LocalTime timestamp = t.parse(stringCellValue);
            System.out.println("time val is {}" + timestamp);
            return timestamp;
        }else {
            return null;
        }
    }



    public static String formatForSch(String stringCellValue) {
        if (!(stringCellValue == "")) {
            SimpleDateFormat before = new SimpleDateFormat("MM/dd/yy");
            SimpleDateFormat after = new SimpleDateFormat("MM/dd/yy");
            Date date = null;
            String result = null;
            try {
                date = before.parse(stringCellValue);
                result = after.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return null;
        }
    }

    public static String formatForTimeOnly(Date stringCellValue, String format) {
        String date = null;
        if (!(stringCellValue == null)) {
            SimpleDateFormat before = new SimpleDateFormat(format);
            try {
                date = before.format(stringCellValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return date;
        }
        return date;
    }



    public static int invoiceDate(long convert) {
        Date result = new Date(convert * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    public static String dateForInvoice(String convert) throws ParseException {
        System.out.println("Inside Date 4 " + convert);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = formatter.parse(convert);
        System.out.println("Inside Date 5 " + date);
        String result = dateNewDateinvoice(date);
        return result;
    }


//
//    private static String dateNewDateinvoice(Date date) {
//        DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
//        ZonedDateTime zdt = ZonedDateTime.parse(date.toString(), f);
//        System.out.println("zonedDate Time Value : -> "+zdt);
//        LocalDate ld = zdt.toLocalDate();
//        System.out.println("localDate Time Value : -> "+ld);
//        DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern("dd-MMM-YY");
//        String output = ld.format(fLocalDate);
//        System.out.println("Inside Date Result " + output);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//        String res = formatter.format(ld);
//        System.out.println("Parsed Local Date Result : - : " + res);
//        return res;
//    }
    private static String dateNewDateinvoice(Date date) {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
      ZonedDateTime zdt = ZonedDateTime.parse(date.toString(), f);
      System.out.println("zonedDate Time Value : -> " + zdt);
      LocalDate ld = LocalDate.of(zdt.getYear(), zdt.getMonth(), zdt.getDayOfMonth());
      System.out.println("localDate Time Value : -> " + ld);
      DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern("dd-MMM-uuuu", Locale.ENGLISH);
      String output = fLocalDate.format(ld);
      System.out.println("Inside Date Result " + output);
      return output;
}
    public static String currentDate(Date date) throws ParseException {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
        ZonedDateTime zdt = ZonedDateTime.parse(date.toString(), f);
        LocalDate ld = zdt.toLocalDate();
        DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern("dd-MM-YY");
        String output = ld.format(fLocalDate);
        System.out.println("Inside current Date>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + output);
        return output;
    }

    public static int givenMonth(long fromDate) {
        Date result = new Date(fromDate * 1000L);
        LocalDate localDate = result.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        return month;
    }

    public static int currentMonth() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        return month;
    }

    public static int previousMonth() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dates = localDate.minusMonths(1);
        System.out.println(dates);
        int month = dates.getMonthValue();
        return month;
    }
}
