package com.meta.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;

public class RSAUtil {

    // 解密密钥
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO97cyaVUwpNteu7QNQ+l8M2FHaQBNM+4OYUlNvcNsYi2EtyZLynD6hGTwl2Nb/CQNjE6nmf/gSTuoxynKv4lYs8bbJ2B8VYtU1ej9isp93tIVs55RXyQ60RBjKAE3HYolAxx8anNM3QXm0FAI+NELItPANGOAo+tMn8E6Q3K5mvAgMBAAECgYB/pZl3E1izwUAaajDf8I0L7CN0HsezoWxWttmxZut7KM+JR2wScimSVAeEz5FOqMfPwwYH1hObdPKWMfD4aQOrr4rTxzdJghqf0BLhX8pK+eVnwse1m2wXA/A6nlOd4w6+W6wxVK1BW+8DYKq90oO7aq95G/MR+bIl0UuAlFougQJBAPsYayzTjlAWoYdMkpyYy+AjUpPLhJHLErbUxt81LAf1ozUEiBI8V7/GqCKaqM9rTnqnhT4brUED6Dv58DTEqp8CQQD0KPXlIerskLQlc/usXTOdL4D7WWG/b7PxfRc24aRr41YNqICtx9fvlRtGfXsemw6FvNR7sejfSpRG/oPn8MbxAkBl/Zkq8BOSPF8IEfZLXVUSicZ0+emzPoUvdmYBLBvib8gHNPwATDpoT0zkIWYIOlH6SCk6/Qr2qZufIPqMtIWJAkEA7VTDcK6/hKRD+AP9p1YRTcFXRvtjIIqcByjo39gF0zlv9GbObh3LsH7nvGVMwDtIqocapAITsrjz2O6dmV9agQJBAIOnW8a40VTGAj1eVilGwxccM/qxVA9TDtPA/3T8KT/sf9OnxH+odDw+e21ItOpOfmRWNY6EFXoD4Ca4XXlluJ4=";

    // 加密密钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGsRg0iO+nuv/25fqZI9VCyZ9uJV/nr7XTPse/qnqhbBA0O8r0/I0h92BcPy4FKbLMMlgpEz+kMTPQqIjRN92K2ALhJKXJBzjEeFPd+QcBml2z4kcBEKwe6zsWUggi+ZLND/3whJz10FzWJ7XpRepXryAY1JEHkkI8bRGF81pUDwIDAQAB";

    private static final RSA rsa = SecureUtil.rsa(PRIVATE_KEY, PUBLIC_KEY);

    // 加密
    public static String encrypt(Object data){
        return rsa.encryptBase64(JSONObject.toJSONString(data), KeyType.PublicKey);
    }

    // 解密
    public static <T> T decrypt(String data, Class<T> type){
        String objectString = rsa.decryptStr(data, KeyType.PrivateKey);
        return JSONObject.parseObject(objectString, type);
    }

    public static void main(String[] args) {
        // RSA
        String PRIVATE_KEY1 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMaxGDSI76e6//bl+pkj1ULJn24lX+evtdM+x7+qeqFsEDQ7yvT8jSH3YFw/LgUpsswyWCkTP6QxM9CoiNE33YrYAuEkpckHOMR4U935BwGaXbPiRwEQrB7rOxZSCCL5ks0P/fCEnPXQXNYntelF6levIBjUkQeSQjxtEYXzWlQPAgMBAAECgYEAgRtoKBLm7y2iz3MnAEV5ePl8gF0G0YyqcIa10wRaxPNsIBTOT5yftHeDBM4FAFPVG7yG7sHUM8PI8Ifix1guurh0khDCRk1jHB/COFJP09rWHRkuO5+f4XL+dig51f+dlp9il0GnbNt3mfXeEn4axtGdw0KcgxWN7CT+tYfgFbECQQD1feck24V/iU1upLFgqkwjjTKBn3oGK1mcR8k9K1sdrBvm5VKpzHRYi97zWXDeoJ0euyb4hZSsQh9X4R3OCrrJAkEAzzJbd79mj6RlDVx6vExL4tRUP1YuVIBqZasdMJBAvFM/EwbRlpqUVrtxHv8B3Iv88NPQ0vZz1G9N4noWMYEsFwJADaVpAuB9BEDioALhpUjyIIvJwfDWfJ9OROSsqAzP7M9TYbtfo/ashPuJcieHoah1825d1TS/te+bBGyMFpb8GQJAUBmvssOT6sQrLNcru8/jJnXfe/zdPF3IxDU6u6OI40VrhPeF3yszXbRpLwp2tcSIrLG2cVhFv0KoYX3BRrIhUQJBANJgsZ9TOALRy2yjo+OjXRUlIolOj7jrB1smduEBkJWqm0zD+ZFblOQIsFnjolCvZq0eIdn+t6FkoH1DG5/acuQ=";
        String PUBLIC_KEY1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDve3MmlVMKTbXru0DUPpfDNhR2kATTPuDmFJTb3DbGIthLcmS8pw+oRk8JdjW/wkDYxOp5n/4Ek7qMcpyr+JWLPG2ydgfFWLVNXo/YrKfd7SFbOeUV8kOtEQYygBNx2KJQMcfGpzTN0F5tBQCPjRCyLTwDRjgKPrTJ/BOkNyuZrwIDAQAB";
        RSA rsa1 = SecureUtil.rsa(PRIVATE_KEY1, PUBLIC_KEY1);
        // 字符串测试（Util加密，第三方解密）
        String string = "test";
        String stringUtilEncrypt = RSAUtil.encrypt(string);
        String stringOtherDecrypt = JSONObject.parseObject(rsa1.decryptStr(stringUtilEncrypt, KeyType.PrivateKey), String.class);
        // 字符串测试（第三方加密，Util解密）
        String stringOtherEncrypt = rsa1.encryptBase64(JSONObject.toJSONString(string), KeyType.PublicKey);
        String stringUtilDecrypt = RSAUtil.decrypt(stringOtherEncrypt, String.class);
        // JSON测试（Util加密，第三方解密）
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key","1");
        jsonObject.put("value","2");
        String jsonUtilEncrypt = RSAUtil.encrypt(jsonObject);
        JSONObject jsonOtherDecrypt = JSONObject.parseObject(rsa1.decryptStr(jsonUtilEncrypt, KeyType.PrivateKey), JSONObject.class);
        // JSON测试（第三方加密，Util解密）
        String jsonOtherEncrypt = rsa1.encryptBase64(JSONObject.toJSONString(jsonObject), KeyType.PublicKey);
        JSONObject jsonUtilDecrypt = RSAUtil.decrypt(jsonOtherEncrypt, JSONObject.class);
    }

}
