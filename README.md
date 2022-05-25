# meta-service-api

## 立即开始

1、请先参阅 **环境变量及说明** 配置好环境变量。

2、初始化数据库，执行 *resource/sql* 文件夹下的 SQL 脚本。

3、启动项目。


## 环境变量及说明

### 如何设置项目端口号？

项目默认的端口号为 **8080** ，需要调整请修改配置文件中 **SERVER_PORT** 的值。
```properties
SERVER_PORT=8080
```

### 如何设置 Redis 连接？

修改配置文件中的 Redis 相关配置。
```properties
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=martin
REDIS_SSL=false
```

### 如何设置 Mysql 连接？

修改配置文件中的 Mysql 相关配置。
```properties
MYSQL_HOST=127.0.0.1:3306
MYSQL_USERNAME=root
MYSQL_PASSWORD=martin
MYSQL_SSL=false
```

### 如何设置客户端的 clientId、clientSecret？

项目自带的认证端仅为 Meta 提供认证服务，客户端仅有 Meta，默认提供一组 clientId、clientSecret。

如果你仅需修改 clientId、clientSecret 配置，请修改以下配置。
```properties
META_CLIENT_ID=meta
META_CLIENT_SECRET=$2a$10$YMIshlb2tH0HkHliyl84tO5eRzCeMiTI59Tqed4OpOw.HjkGZvE7i
```

### 如何添加免鉴权接口？

默认所有接口都需要进行 token 验证，不需要验证的接口可以添加到下面的白名单配置中。
```properties
WHITE_LIST_URL=/oauth/token,/token
```

### 如何配置 token 失效时间及加密密钥？

默认使用 **HMAC-SHA-256** 随机生成的密钥，需要调整请修改配置文件中 **JWT_SIGNING_KEY** 的值。

默认的 access token 过期时间为1天，refresh token 过期时间为15天。
```properties
JWT_SIGNING_KEY=pbiRBFxfdeDVm4VDVrTy72v9DC+2L5vbclMNM2iO2SE=
ACCESS_TOKEN_VALIDITY_SECONDS=86400
REFRESH_TOKEN_VALIDITY_SECONDS=1296000
```
使用非对称加密的方式生成 token 需要改写以下代码：

默认加密方式
```
jwtAccessTokenConverter.setSigningKey(jwt_signing_key);
```
非对称加密方式
```
tAccessTokenConverter.setKeyPair(keyPair);
```

### 如何修改小程序配置？

项目提供了微信小程序认证方式，修改以下配置以支持小程序认证。
```properties
WECHAT_APPID=
WECHAT_SECRET=
```

### 如何发送短信？

项目默认对接的 **云片( https://www.yunpian.com)** 短信发送平台，如果使用 **云片** 只需要修改 **SMS_API_KEY** 配置及短信模版配置。
```properties
SMS_API_KEY=
BINDING_SMS_TID=
```
如果不使用 **云片**，需要修改 **SmsUtil** 中的相关代码。










