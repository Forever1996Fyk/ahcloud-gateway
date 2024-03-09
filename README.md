# Ahcloud-Gateway


## 😺 项目起源

- 目前市面上的开源网关项目，功能过于单一，无法应用到实际的项目上
- SpringGateway是SpringCloud最近非常流行的网关框架，但提供的动态路由过于简单
- 在网关管理方面，包括：API管理，路由管理，限流管理，黑白名单管理等一系列功能上，没有统一的管理平台

因此本项目基于SpringGateway进行开发，解决上面所提到的问题。

> ✨如果该项目对您有帮助，您的star是我不断优化的动力！！！
>
> - [github点击前往](https://github.com/MyMonsterCat/RapidOcr-Java)
> - [gitee点击前往](https://gitee.com/MichaelFan/ahcloud-gateway)

## 👏 项目特点

- 基于流行的SpringGateway开发
- 集成Nacos作为配置中心，可动态配置路由和API接口
- 提供完整的管理平台（已集成到[EDAS](https://gitee.com/MichaelFan/edas)项目中）

## 🎉 快速开始

### 1️⃣ 环境配置

本项目依赖于SpringCloud和SpringCloudAlibaba（后续考虑基于SpringBoot开发），
配置中心采用Nacos，所以在启动项目的前提是，需要启动Naocs服务。

- Nacos版本 > 2.0

需要准备MySQL > 8.0，并执行SQL。要注意的是， 
- 本项目需要配合[EDAS](https://gitee.com/MichaelFan/edas)中的网关服务，**如果您已经部署了网关服务，那么数据库两者服务必须要统一。**
- 如果还没有部署EDAS中的网关服务，单独启动，那么可以在doc/sql目录下，直接执行SQL也可。**但是与EDAS中网关服务也必须统一数据库。**

### 2️⃣ 准备工作

当环境配置好了后，只需要修改 gateway-server模块下的配置文件即可启动。
由于本项目采用的多环境部署，这里以local环境为例。
```yaml
spring:
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
      config:
        namespace: ddd56e16-f935-4987-9539-6b51ac509a4e
        max-retry: 10
        config-long-poll-timeout: 5
        config-retry-time: 5
        refresh-enabled: true
        username: nacos
        password: nacos
        group: com.ahcloud.gateway.server
        file-extension: yaml
      discovery:
        enabled: true
        server-addr: 127.0.0.1:8848
        namespace: ddd56e16-f935-4987-9539-6b51ac509a4e
        group: local
```
修改对应上面配置nacos地址、用户名密码、namespace。
> 这里的namespace取的是id，也可以选择name，group也可以自定义
---
当上面的bootstrap-local.yml配置完后，就需要在对应的nacos命名空间下创建配置文件。
> Naocs中的配置文件
> - namespace=ddd56e16-f935-4987-9539-6b51ac509a4e
> - data-id=ahcloud-gateway-server-local.yaml
> - type=yaml
> - group = com.ahcloud.gateway.server

项目中已经提供了对应的示例*application-example.yml*。
只需要将其mysql地址修改，在复制到nacos的配置中即可。

### 3️⃣ 启动项目

直接`GatewayServerApplication.java`启动项目即可。

### 4️⃣ 接入文档

- 文档：https://www.yuque.com/u2194782/ahcloud/apl8wuyyinbi7sv3

> 一般情况下，网关服务的使用场景是分布式项目，所以如果想接入Ahcloud-gatewa还需要再其他项目中引入其依赖，否则是没有意义的。
> 那么引入依赖的方案，通常情况下是使用自己公司的中Nexus私服。 
> 如果没有私服，也可以通过自己打jar包的方式，在引入项目中也可。参考 https://www.cnblogs.com/204Handsome/p/11848569.html

这里只介绍通过引入Maven依赖来接入。
```xml
<dependency>
    <groupId>com.ahcloud.gateway</groupId>
    <artifactId>gateway-spring-boot-starter</artifactId>
    <version>${gateway.spring.boot.starter.version}</version>
</dependency>
```
## 📌 TODO

- [x] 整合Sentinel开发动态黑白名单
- [x] 动态跨域开发
- [x] 整合Sentinel开发接口限流(正在实现中)
- [x] 整合prometheus实现网关性能监控

## 鸣谢

- [Apache Shenyu](https://github.com/apache/shenyu.git)

## 开源许可

使用 [MIT](https://gitee.com/MichaelFan/ahcloud-gateway/blob/master/LICENSE)
