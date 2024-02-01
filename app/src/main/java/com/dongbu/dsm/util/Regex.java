package com.dongbu.dsm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel Park on 4/13/15.
 */
public class Regex {

    private static Regex sThis = null;

    /**
     *
     * ^ : Start of the line <br>
     * [a-z0-9_-] : Match characters and symbols in the list, a-z, 0-9, underscore, hyphen <br>
     * {3,15} : Length at least 3 characters and maximum length of 15 <br>
     * $ : End of the line
     *
     */
    private final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";




    /**
     *
     * ( : Start of group <br>
     * (?=.*\d) : must contains one digit from 0-9 <br>
     * (?=.*[a-z]) : must contains one lowercase characters <br>
     * (?=.*[A-Z]) : must contains one uppercase characters <br>
     * (?=.*[@#$%]) : must contains one special symbols in the list "@#$%" <br>
     * . : match anything with previous condition checking <br>
     * {6,20} : length at least 6 characters and maximum of 20 <br>
     * ) : End of group
     *
     */
    private final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,12})";


    ///////////////////////////////////////////////////////////////////////////////////////
    //WC
    //6~12자 영문대소문자, 숫자, 사용가능
    private final String ID_PATTERN = "^[a-zA-Z0-9]{8,8}$";

    //6~6자 영문대문자, 숫자, 사용가능
    private final String CARSENO_PATTERN = "^[A-Z0-9]{6,6}$";

    //6~12자 영문대소문자, 숫자, 사용가능
    //private final String CARNO_PATTERN = "^[0-9a-zA-Z가-힣_-]{15,15}$";
    private final String CARNO_PATTERN = "^[0-9가-힣_-]{14,15}$";

    // 한글10자, 영문20자, 한글,영문,숫자 사용가능
    //private final String NAME_PATTERN = "^[0-9a-zA-Z가-힣]{2,10}$";
    private final String NAME_PATTERN = "^[0-9가-힣]{2,10}$";

    // 8자 영문대소문자, 숫자, 특수문자 혼합하여 사용
    private final String PW_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-]|.*[0-9]).{8,8}$";

    private final String BIRTH_DAY_PATTERN = "^[0-9]{8,8}$";

    private final String TEL_PATTERN = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

    private final String ONLY_NUMBER = "^[0-9]*$";

    private final String ONLY_ALPHABET = "^[a-zA-Z]*$";

    private final String ONLY_HANGUL = "^[가-힣]*$";

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static Regex getInstance() {
        if (sThis == null) {
            sThis = new Regex();
        }
        return sThis;
    }


    public Regex() {

    }

    /**
     * Convert input String to alphabets only. [a-zA-Z] <br>
     *      e.g) "03de8283ffe&*&" -> "deffe"
     * @param word input String
     * @return
     */
    public String convertToOnlyAlphabets(String word){

        Log.i("Previous word : " + word);

        String changedWord = "";

        Log.i("Online word : " + word);
        // Only alphabet characters are accepted..
        String[] a = word.split("");
        List<String> newVoca = new ArrayList<String>();
        for(String str : a){
            if(Pattern.matches("[a-zA-z]+", str)){
                newVoca.add(str);
                Log.i("patterning string : " + str);
            }
        }
        // List to String
        for(String s : newVoca){
            changedWord += s;
        }

        Log.i("Changed word :" + word);
        return changedWord;
    }

    public boolean IsIDCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(ID_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsCarNoCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(CARNO_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsCarSeNoCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(CARSENO_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }



    public boolean IsNameCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }


    /**
     * Check if this password is right for our regulation.
     *
     * @param input
     * @return
     */
    public boolean IsPasswordCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(PW_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsEmailCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsBirthdayCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(BIRTH_DAY_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsTelephoneCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(TEL_PATTERN);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsOnlyNumberCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(ONLY_NUMBER);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsOnlyAlphabetCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(ONLY_ALPHABET);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public boolean IsOnlyHangulCorrectByPattern(String input) {

        Pattern pattern = Pattern.compile(ONLY_HANGUL);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

}
