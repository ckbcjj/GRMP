package com;

import com.mfc.aes.AESTools;

public class AESTest {

    public static String OTA_API_HOST = "ad.valor.database.host";
    public static String OTA_API_MASTER = "ad.valor.database.master";
    public static String OTA_API_USER = "ad.valor.database.user";
    public static String OTA_API_PASS = "ad.valor.database.password";

    public static String BASE64_KEY = "Ebw6MyVpHAC23ZMszboDDQ";

    public static String encrypt(String content, String key, String item) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(item).append("=").append(AESTools.encrypt(content, key));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        new Student("hands").dosth();
        /*System.out.println("#------------------------UAT( LIVETV-OVH-UAT01 158.69.220.68)--------------------------#");
        System.out.println("\n##OTA API##");
        System.out.println(AESTest.encrypt("127.0.0.1:3306", BASE64_KEY, OTA_API_HOST));
        System.out.println(AESTest.encrypt("upms_db", BASE64_KEY, OTA_API_MASTER));
        System.out.println(AESTest.encrypt("root", BASE64_KEY, OTA_API_USER));
        System.out.println(AESTest.encrypt("123456", BASE64_KEY, OTA_API_PASS));
        System.out.println("#------------------------UAT--------------------------#");*/

    }
}