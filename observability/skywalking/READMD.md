# SkyWalking

官网：https://skywalking.apache.org/

## 版本

SkyWalking: 9.0.4
JDK: 1.8
Spring Boot: 2.3.2.RELEASE
MySQL: 5.7.29

## SkyWalking+MySQL

修改`config/application.yml`

```yaml
storage:
  selector: mysql
  mysql:
    properties:
      jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://localhost:3306/skywalking?rewriteBatchedStatements=true&allowMultiQueries=true&serverTimezone=GMT"}
```

修改`webapp/application.yml`，设置相关端口防止端口冲突

Windows环境启动`bin/startup.bat`

## JVM参数

```
-javaagent:D:\skywalking-agent\skywalking-agent.jar
-Dskywalking.agent.service_name=my-skywalking
-Dskywalking.collector.backend_service=192.168.56.103:11800
```