package com.example.ocecreditcardlibrary.util;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.ArrayList;

/**
 * Created by kanthimp on 14/12/2560.
 */

public class Card {
    String strNum = "";
    String checkExp = "";
    boolean number;
    private String message;
    private ArrayList<EntityAnnotation> arrayList;
    private String idCard;
    private String expDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public ArrayList<EntityAnnotation> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<EntityAnnotation> arrayList) {
        this.arrayList = arrayList;
    }


    public void cutSting() {
        String[] arrayString = message.split("\n");
        String arrStr = arrayString.toString();

        checkIdCardFromArrayList();
        findExpireDate();


    }


    private void checkIdCardFromArrayList() {

        String idCardNum = "";
        for (EntityAnnotation item : arrayList) {
            String tmpMessage = item.getDescription();
            // แบ่งชุดจาก \n
            String[] arrayString = tmpMessage.split("\n");
            //วนลูป\n
            for (int i = 0; i < arrayString.length; i++) {
                //แบ่งชุดจากเว้นวรรค
                String[] arrayplatter = arrayString[i].split(" ");
                if (arrayplatter.length == 4) {
                    //เช็ค4ชุด
                    for (int c = 0; c < arrayplatter.length; c++) {
                        strNum = arrayplatter[c];
                        validateNumber(strNum);
                        idCardNum = idCardNum + strNum;
                    }

                }
            }
            setIdCard(idCardNum);
        }


    }

    private String validateNumber(String string) {
        if (string.length() == 4) {
            //เช็คแต่ละชุดครบ4
            String tmpNum = "";
            String[] tmpNumber = string.split("");
            for (int h = 1; h < tmpNumber.length; h++) {
                if (isInteger(tmpNumber[h])) {
                    tmpNum = tmpNum + tmpNumber[h];
                } else {
                    tmpNum = tmpNum + "*";
                }
            }
            strNum = tmpNum;
        } else if (string.length() < 4) {
            String tmpNum = "";
            String[] tmpNumber = string.split("");
            for (int h = 1; h < tmpNumber.length; h++) {
                if (isInteger(tmpNumber[h])) {
                    tmpNum = tmpNum + tmpNumber[h];
                }
            }
            while (tmpNum.length() < 4) {
                tmpNum = tmpNum + "*";
            }
            strNum = tmpNum;

        } else if (string.length() > 4) {

            String tmpNum = "";
            String[] strNumber = string.split("");
            for (int h = 1; h < strNumber.length; h++) {
                if (tmpNum.length() < 4) {
                    if (isInteger(strNumber[h])) {
                        tmpNum = tmpNum + strNumber[h];
                    } else {
                        tmpNum = tmpNum + "*";
                    }
                }
            }
            strNum = tmpNum;

        }

        return strNum;
    }

    public boolean isInteger(String input) {
        try {
            Integer val = Integer.valueOf(input);
            if (val != null)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void unitTest_validateNumber() {
        String case1 = "21";
        String case2 = "213647";
        String case3 = "2154";
        String case4 = "21bA";
        String result1 = validateNumber(case1);
        if (result1.equals("21**")) {
            System.out.println(result1 + " error !!!");
        }

        String result2 = validateNumber(case2);
        if (result2.equals("2136")) {
            System.out.println(result2 + " error !!!");
        }

        String result3 = validateNumber(case3);
        if (result3.equals("2154")) {
            System.out.println(result3 + " error !!!");
        }

        String result4 = validateNumber(case4);
        if (result4.equals("21**")) {
            System.out.println(result4 + " error !!!");
        }

    }

    private void unitTest_isInteger() {
        boolean bCheck = false;
        String a = "a";
        bCheck = isInteger(a);

        if (bCheck == true) {
            System.out.println(a + " is not number" + ",Function isInteger have do something wrong,let fix it!!!.");
            return;
        }

        String b = "9";

        bCheck = isInteger(b);

        if (bCheck == false) {
            System.out.println(b + " is a number" + ",Function isInteger have do something wrong,let fix it!!!.");
            return;
        }

    }

    private String findExpireDate() {
        String tmpExp = "";
        for (EntityAnnotation item : arrayList) {
            String tmpMessage = item.getDescription();
            String[] arrText = tmpMessage.split("\n");
            for (int i = 0; i < arrText.length; i++) {
                String exp = validateExpireDate(arrText[i]);
                if (exp.contains("/")) {
                    //functionเสริม
                    return exp;
                }
            }
        }
        return "";
    }

    private String validateExpireDate(String findExp) {
        String assumeExpireDate = "";
        String expireDateString = "";

        String[] txtExpire = findExp.split(" ");
        if (txtExpire.length >= 2) {
            String expireDate = "";
            for (int i = 0; i < txtExpire.length; i++) {
                if (txtExpire[i].contains("/")) {
                    expireDate = txtExpire[i];
                }
            }
            for (int j = 0; j < expireDate.length(); j++) {
                assumeExpireDate = expireDate.trim();
                if (assumeExpireDate.contains("/")) {
                    expireDateString = assumeExpireDate;
                } else {
                    expireDateString = "";
                }
            }


        } else {
            assumeExpireDate = txtExpire[0].trim();
            if (assumeExpireDate.contains("/")) {
                expireDateString = assumeExpireDate;
            } else {
                expireDateString = "";
            }
        }
        if (!expireDateString.equals("")) {
            setExpDate(expireDateString);
        }
        return expireDateString;
    }


    private void unitTest_validateExpireDate() {
        String expString = "30ad EXP 20/17";
        String expireDate = validateExpireDate(expString);
        if (!expireDate.equals("20/17")) {
            System.out.println("EXP : Expire date is not match,it must be 20/27 but you got" + expireDate + ".");
        }

        String dashString = "2/18 - 20/17";
        expireDate = validateExpireDate(dashString);
        if (!expireDate.equals("20/17")) {
            System.out.println("EXP : Expire date is not match,it must be 20/27 but you got" + expireDate + ".");
        }

        String spaceString = "2/02 20/27";
        expireDate = validateExpireDate(spaceString);
        if (!expireDate.equals("20/17")) {
            System.out.println("EXP : Expire date is not match,it must be 20/27 but you got" + expireDate + ".");
        }

        String onlyOneString = "20/17";
        expireDate = validateExpireDate(onlyOneString);
        if (!expireDate.equals("20/17")) {
            System.out.println("EXP : Expire date is not match,it must be 20/27 but you got" + expireDate + ".");
        }

        String notExpireDate = "I am Muk";
        expireDate = validateExpireDate(notExpireDate);
        if (!expireDate.equals("20/17")) {
            System.out.println("Something wrong" + expireDate + ".");
        }

    }


}




