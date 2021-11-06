# meta-service-api

## 与第三方系统交互

与第三方系统交互采用了 RSA 非对称加密算法，第三方系统需要维护下列密钥对。使用公钥加密请求数据，使用私钥解密响应数据。

公钥：

MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDve3MmlVMKTbXru0DUPpfDNhR2kATTPuDmFJTb3DbGIthLcmS8pw+oRk8JdjW/wkDYxOp5n/4Ek7qMcpyr+JWLPG2ydgfFWLVNXo/YrKfd7SFbOeUV8kOtEQYygBNx2KJQMcfGpzTN0F5tBQCPjRCyLTwDRjgKPrTJ/BOkNyuZrwIDAQAB

私钥：

MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMaxGDSI76e6//bl+pkj1ULJn24lX+evtdM+x7+qeqFsEDQ7yvT8jSH3YFw/LgUpsswyWCkTP6QxM9CoiNE33YrYAuEkpckHOMR4U935BwGaXbPiRwEQrB7rOxZSCCL5ks0P/fCEnPXQXNYntelF6levIBjUkQeSQjxtEYXzWlQPAgMBAAECgYEAgRtoKBLm7y2iz3MnAEV5ePl8gF0G0YyqcIa10wRaxPNsIBTOT5yftHeDBM4FAFPVG7yG7sHUM8PI8Ifix1guurh0khDCRk1jHB/COFJP09rWHRkuO5+f4XL+dig51f+dlp9il0GnbNt3mfXeEn4axtGdw0KcgxWN7CT+tYfgFbECQQD1feck24V/iU1upLFgqkwjjTKBn3oGK1mcR8k9K1sdrBvm5VKpzHRYi97zWXDeoJ0euyb4hZSsQh9X4R3OCrrJAkEAzzJbd79mj6RlDVx6vExL4tRUP1YuVIBqZasdMJBAvFM/EwbRlpqUVrtxHv8B3Iv88NPQ0vZz1G9N4noWMYEsFwJADaVpAuB9BEDioALhpUjyIIvJwfDWfJ9OROSsqAzP7M9TYbtfo/ashPuJcieHoah1825d1TS/te+bBGyMFpb8GQJAUBmvssOT6sQrLNcru8/jJnXfe/zdPF3IxDU6u6OI40VrhPeF3yszXbRpLwp2tcSIrLG2cVhFv0KoYX3BRrIhUQJBANJgsZ9TOALRy2yjo+OjXRUlIolOj7jrB1smduEBkJWqm0zD+ZFblOQIsFnjolCvZq0eIdn+t6FkoH1DG5/acuQ=

### 自行生成密钥：

如果需要替换加解密钥请自行生成，生成后需要同步替换 RSAUtil 中的公私钥。

    注：需要生成两组 RSA 密钥对，其中"密钥对1"的公钥和"密钥对2"的私钥为一组用于第三方系统或本系统，
       "密钥对1"的私钥和"密钥对2"的公钥为另一组。

### 加解密示例：

java:
```
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
```

## 接口返回说明

接口返回示例：

```
{
  "code": "状态码",
  "data": {},
  "msg": "解释"
}
```

接口状态码“C-00000”表示请求成功，非“C-00000”表示业务出现异常，状态码及对应的解释如下：

|状态码|解释|
|---|---|
| C-00000 | ok |
| C-00001 | 网络异常 |
| C-00002 | Token过期或已失效 |
| C-00003 | 没有文件操作权限 |
| C-00004 | 不是文件夹类型 |
| C-00005 | 文件已删除 |
| C-00006 | 文件不存在 |
| C-00007 | 文件不存在有效地址 |
| C-00008 | 下载失败 |
| C-00009 | 登录失败 |
| C-00010 | code无效 |
| C-00011 | 登录频率限制 |
| C-00012 | 高风险用户 |
| C-00013 | 添加水印失败 |
| C-00014 | 原有父级文件夹不存在 |
| C-00015 | 根目录不能被删除 |
| C-00016 | 不能使用根目录命名 |
| C-00017 | 该分享已被禁止其他用户访问 |
| C-00018 | 该分享已被禁止其他用户分享 |
| B-00001 | 参数不正确 |
| O-00001 | 第三方调用失败 |

## 环境变量说明

```
# 服务端口号
SERVER_PORT=7050

# redis 配置
REDIS_HOST=redis-master.system-stream-dev1.svc.cluster.local
REDIS_PORT=6379
REDIS_PASSWORD=bite

# 数据库配置
DATASOURCE_DBHOST=mysql.system-stream-dev1.svc.cluster.local:3306
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=bite

# 七牛云配置
qiu.accessKey=
qiu.secretKey=
qiu.bucket.name=
qiu.domain=

# 小程序配置
wechat.appid=
wechat.secret=
```


