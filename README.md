# meta-service-api

## 环境变量及说明

### 如何设置项目端口号？

项目默认的端口号为 **8080** ，如果需要调整请修改配置文件中的 **SERVER_PORT** 。
```properties
SERVER_PORT=8080
```

### 如何设置 Redis 连接？

修改配置文件中的 Redis 相关配置。
```properties
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=martin
```

### 如何设置 Mysql 连接？

修改配置文件中的 Mysql 相关配置。
```properties
DATASOURCE_DBHOST=127.0.0.1:3306
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=martin
```

### 如何设置客户端的 clientId、clientSecret？

项目自带的认证端仅为 Meta 提供认证服务，客户端仅有 Meta，默认提供一组 clientId、clientSecret。

如果你仅需修改 clientId、clientSecret 配置，请修改以下配置。
```properties
META_CLIENT_ID=meta
META_CLIENT_SECRET=$2a$10$YMIshlb2tH0HkHliyl84tO5eRzCeMiTI59Tqed4OpOw.HjkGZvE7i
```







