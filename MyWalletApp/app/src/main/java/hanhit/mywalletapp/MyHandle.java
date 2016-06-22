package hanhit.mywalletapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hanh.tran on 6/22/2016.
 */
public class MyHandle {

    public MyHandle() {}

    public String handleString(String str) {
        String newString;
        switch (str.length()) {
            case 4:
                newString = new StringBuilder(str).insert(1, ",").toString();
                break;
            case 5:
                newString = new StringBuilder(str).insert(2, ",").toString();
                break;
            case 6:
                newString = new StringBuilder(str).insert(3, ",").toString();
                break;
            case 7:
                newString = new StringBuilder(str).insert(1, ",").insert(5, ",").toString();
                break;
            case 8:
                newString = new StringBuilder(str).insert(2, ",").insert(6, ",").toString();
                break;
            case 9:
                newString = new StringBuilder(str).insert(3, ",").insert(7, ",").toString();
                break;
            default:
                newString = str;
                break;
        }
        return newString;
    }

    public String handBackSpace(String str) {
        String newStr;
        switch (str.length()) {
            case 5:
                newStr = new StringBuilder(str).insert(2, ",").toString();
                break;
            case 4:
                newStr = new StringBuilder(str).insert(1, ",").toString();
                break;
            default:
                newStr = str;
                break;
        }
        return newStr;
    }

    // Method format date
    public String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public String convertMonthTypeStringToNum(String month){
        switch (month){
            case "JANUARY":
                return "01";
            case "FEBRUARY":
                return "02";
            case "MARCH":
                return "03";
            case "APRIL":
                return "04";
            case "MAY":
                return "05";
            case "JUNE":
                return "06";
            case "JULY":
                return "07";
            case "AUGUST":
                return "08";
            case "SEPTEMBER":
                return "09";
            case "OCTOBER":
                return "10";
            case "NOVEMBER":
                return "11";
            default:
                return "12";
        }
    }

}
