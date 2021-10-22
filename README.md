# meta-service-api

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


