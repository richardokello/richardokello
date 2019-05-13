/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author emuraya
 */
@Service
public class PasswordGenerator {

    public PasswordGenerator() {
    }

//    @Autowired
//    UfsSysConfigRepository passRepo;

    //  character lengths as defined in db
    public static String CHAR_L = "abcdefghijklmnopqrstuvwxyz";
    public static String CHAR_U = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String NUM = "1234567890";
    public static String CHAR_S = "!@#$%^&*()_=+";

    public String generateRandomPassword() {

//        int CHAR_U_COUNT = Integer.parseInt(passRepo.getConfiguration("Password Policy", "upperCase").getValue());
//        int CHAR_L_COUNT = Integer.parseInt(passRepo.getConfiguration("Password Policy", "lowerCase").getValue());
//        int PASSWORD_LENGTH = Integer.parseInt(passRepo.getConfiguration("Password Policy", "passwordLength").getValue());
//        int NUM_COUNT = Integer.parseInt(passRepo.getConfiguration("Password Policy", "numericLength").getValue());
////        int charLength = Integer.valueOf(passRepo.passUppercase("Password Policy", "charLength"));
//        int CHAR_S_COUNT = Integer.parseInt(passRepo.getConfiguration("Password Policy", "specialCharacter").getValue());

//        System.out.println("CHARACTER : " + CHAR_U_COUNT);

        StringBuffer randPass = new StringBuffer();
//        char ch;
//        for (int i = 0; i < PASSWORD_LENGTH; i++) {
//            if (randPass.length() < PASSWORD_LENGTH) {
//                ch = CHAR_L.charAt(getRandomNumber(CHAR_L.length()));
//                randPass.append(ch);
//            }
//            if (randPass.length() < PASSWORD_LENGTH) {
//                ch = CHAR_U.charAt(getRandomNumber(CHAR_U.length()));
//                randPass.append(ch);
//            }
//            if (randPass.length() < PASSWORD_LENGTH) {
//                ch = NUM.charAt(getRandomNumber(NUM.length()));
//                randPass.append(ch);
//            }
//            if (randPass.length() < PASSWORD_LENGTH) {
//                ch = CHAR_S.charAt(getRandomNumber(CHAR_S.length()));
//                randPass.append(ch);
//            }
//        }
        return randPass.toString();
    }

    private static final String ALPHA_CAPS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijkmnpqrstuvwxyz";
    //    private static final String NUM = "23456789";
    private static final String SPL_CHARS = "!@#$%^&*()_=+";

    public char[] generatePswd() {

//        int noOfCAPSAlpha = Integer.parseInt(passRepo.getConfiguration("Password Policy", "upperCase").getValue());
//        int CHAR_L_COUNT = Integer.parseInt(passRepo.getConfiguration("Password Policy", "lowerCase").getValue());
//        int minLen = Integer.parseInt(passRepo.getConfiguration("Password Policy", "passwordLength").getValue());
//        int maxLen = Integer.parseInt(passRepo.getConfiguration("Password Policy", "maxPasswordLength").getValue());
//        int noOfDigits = Integer.parseInt(passRepo.getConfiguration("Password Policy", "numericLength").getValue());
//        int noOfSplChars = Integer.parseInt(passRepo.getConfiguration("Password Policy", "specialCharacter").getValue());

//        Random rnd = new Random();
//        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
//        char[] pswd = new char[len];
//        int index = 0;
//
//        for (int i = 0; i < noOfCAPSAlpha; i++) {
//            index = getNextIndex(rnd, len, pswd);
//            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
//        }
//        for (int i = 0; i < noOfDigits; i++) {
//            index = getNextIndex(rnd, len, pswd);
//            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
//        }
//        for (int i = 0; i < noOfSplChars; i++) {
//            index = getNextIndex(rnd, len, pswd);
//            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
//        }
//        for (int i = 0; i < len; i++) {
//            if (pswd[i] == 0) {
//                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
//            }
//        }
//        return pswd;
        return null;
    }

    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        while (pswd[index = rnd.nextInt(len)] != 0) ;
        return index;
    }

    private int getRandomNumber(int index) {
        System.out.println("INDEX ================================" + index);

        int randomInt = 0;
        Random randomGenerator = new Random();
        if ((index - 1) == 0) {
            randomInt = randomGenerator.nextInt(index);
        } else {
            randomInt = randomGenerator.nextInt(index - 1);
        }
        return randomInt;
    }

//    public String generatePassword() {
//        String password = null;
//        String uppercase = passRepo.passUppercase("Password Policy", "upperCase");
//        String lowercase = passRepo.passUppercase("Password Policy", "lowerCase");
//        String passwordLength = passRepo.passUppercase("Password Policy", "passwordLength");
//        String numericLength = passRepo.passUppercase("Password Policy", "numericLength");
//        String charLength = passRepo.passUppercase("Password Policy", "charLength");
//        String specialCharacter = passRepo.passUppercase("Password Policy", "specialCharacter");
//
////       1. GET CONFIGURATIONS
//        char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?")).toCharArray();
//        String randomStr = RandomStringUtils.random(8, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
//        System.out.println(randomStr);
//
//        return password;
//    }
}
