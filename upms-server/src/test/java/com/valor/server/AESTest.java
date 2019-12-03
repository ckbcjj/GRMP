package com.valor.server;

import com.mfc.aes.AESTools;
import com.valor.model.web.request.RequestType;

import java.text.SimpleDateFormat;
import java.util.*;


public class AESTest {

	public static String AD_SERVER_HOST = "com.valor.valor.db.host";
	public static String AD_SERVER_INST = "com.valor.valor.db.inst";
	public static String AD_SERVER_USER = "com.valor.valor.db.user";
	public static String AD_SERVER_PASS = "com.valor.valor.db.password";

	public static String BASE64_KEY = "Ebw6MyVpHAC23ZMszboDDQ";

	public String encrypt(String content, String key, String item) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(item).append("=").append(AESTools.encrypt(content, key));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String decrypt(String content, String key, String item) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(item).append("=").append(AESTools.encrypt(content, key));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}


	public static void main(String[] args) {

		Set set1 = new HashSet(){{addAll(Arrays.asList(1,2,3,4,4,5));}};

		List list = Arrays.asList(1,2,7,8,8,9);

		set1.addAll(list);

		System.out.println(set1);

		/*System.out.println("\n##AD SERVER##");
		System.out.println(aesTest.encrypt("127.0.0.1:3306", BASE64_KEY, AD_SERVER_HOST));
		System.out.println(aesTest.encrypt("upms_db", BASE64_KEY, AD_SERVER_INST));
		System.out.println(aesTest.encrypt("root", BASE64_KEY, AD_SERVER_USER));
		System.out.println(aesTest.encrypt("123456", BASE64_KEY, AD_SERVER_PASS));
		System.out.println("#------------------------PROD--------------------------#");*/

	}
}


