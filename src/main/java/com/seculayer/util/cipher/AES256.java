package com.seculayer.util.cipher;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
 */
public class AES256 {
    private String iv;
    private Key keySpec;
    private final String defaultCharset = "UTF-8";

    public AES256() {
    	try {
	    	String tmpKey = "AES/CBC/PKCS5Padding";
	        byte[] keyBytes = new byte[16];
	        byte[] b = tmpKey.getBytes(defaultCharset);
	        int len = b.length;
	        if(len > keyBytes.length){
	            len = keyBytes.length;
	        }
	        System.arraycopy(b, 0, keyBytes, 0, len);
	        this.iv = new String(keyBytes, defaultCharset);
	        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
	
	        this.keySpec = keySpec;
    	} catch(UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     * @param key 암/복호화를 위한 키값
     * @throws UnsupportedEncodingException
     */
    public AES256(String key) throws UnsupportedEncodingException {
    	String tmpKey = key;
    	do {
    		tmpKey += "0";
    		//System.out.println(tmpKey);
    	} while(tmpKey.getBytes(defaultCharset).length < 16);
    	
        //this.iv = tmpKey.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = tmpKey.getBytes(defaultCharset);
        int len = b.length;
        if(len > keyBytes.length){
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        this.iv = new String(keyBytes, defaultCharset);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }

    /**
     * AES256 으로 암호화 한다.
     * @param str 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException{
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes(defaultCharset));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     * @param str 복호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), defaultCharset);
    }
    
    //Reference URL : http://blog.kindler.io/archives/121
    public static void main(String[] args) {    	
    	try {
    		//String key = "se1ule!@#$%^r1234567890";
    		//AES256 encUtil = new AES256(key);
    		AES256 encUtil = new AES256();
    		for(String s:args) {
    			System.out.println("> source=["+s+"], encrypted=["+encUtil.encrypt(s)+"]");
    		}
    		
//    		String source = "This is test...한글테스트입니다.~~~!@#$%Z(90A*&^A%";
//			String encStr = encUtil.encrypt(source);
//			System.out.println("> encStr=[" + encStr + "]");
//			String decStr = encUtil.decrypt(encStr);
//			System.out.println("> decStr=[" + decStr + "]");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
    }
}