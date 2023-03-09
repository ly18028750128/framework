# 基础开发框架

## 项目总述
 - 框架采用目前成熟的springcloud框架，配置和服务发现中心采用consul，SpringBoot2+MybatisPlus+SpringSecurity+redis+mysql+mongodb+Vue的前后端分离技术，支持多种登录方式，诣在构建一个应用开发平台，基于框架的基础的功能，用户可以在此基础上很快的进行相关业务代码的开发。
 - 基础包含用户管理、角色管理、菜单管理、数据字典管理、动态接口配置管理、文件管理、公告管理、邮件管理、图片管理、定时任务管理、微服务管理。
 - 前台管理代码仓库地址：https://gitee.com/longyou/admin-front-block
 - 后台管理界面截图
   ![后台管理页面](https://mmbiz.qpic.cn/mmbiz_png/febiagFeCN14RY5ianDxUvIfox6M19l81RsibashSO8J8hwuNEr4WJN5T4W96xgZGKdUmMd2Fvwa3exicBgtH39yXw/0?wx_fmt=png "后台管理页面")
   
## 核心依赖

| 依赖              | 版本    |
|-----------------|-------|
| Spring Boot     | 2.7.3 |
| weixin-java     | 4.4.0 |
| Spring Security | 2.7.3 |
| Mybatis Plus    | 3.5.2 |
| spring-cloud    | 3.1.3 |
| swagger         | 3.0.0 |

## 本地安装
### 基本环境（必备）
1. JDK：8+
2. Redis 3.0+
3. Maven 3.0+
4. MYSQL 5.7+
5. Node v8+
6. consul 1.0+
### 项目启动

1. 请确保redis已经安装启动
2. 请确保mongdodb已经安装启动 
3. 下载代码
```
git clone https://gitee.com/longyou/springcloud-tester.git
```
4. spring-gateway启动参数
   1. -Dserver.port=8085 -Dsystem.config.consul.host=192.168.30.239（自行更改） -Dsystem.config.consul.port=10050 -Dsystem_config_consul_instance_ip=192.168.30.192 -Dsystem.validate.check=true -Dsystem.mfa.enabled=true -Dfastjson.parser.autoTypeSupport=true -Xms256m -Xmx512m -Xss512K
   2. spring.application.group=YANSHI-LONGYOU-;sys.curr_profile=yanshi;spring.profiles.active=yanshi;system.rabbitmq.enabled=fasle;system.rabbitmq.receive.enabled=fasle
5. common-service启动参数
   1. -Dserver.port=8085 -Dsystem.config.consul.host=192.168.30.239（自行更改） -Dsystem.config.consul.port=10050 -Dsystem_config_consul_instance_ip=192.168.30.192 -Dsystem.validate.check=true -Dsystem.mfa.enabled=true -Dfastjson.parser.autoTypeSupport=true -Xms256m -Xmx512m -Xss512K
   2. spring.application.group=YANSHI-LONGYOU-;sys.curr_profile=yanshi;spring.profiles.active=yanshi;system.rabbitmq.enabled=fasle;system.rabbitmq.receive.enabled=fasle
6. 配置中心参考配置请参考```doc/配置中心配置示例``` 
7. docker启动参数，请参考各项目下的配置
8. SWAGGER配置中心地址 http://网关IP:端口号/doc.html
## 系统模块介绍
### 个人中心
### 系统管理
### 系统设置
## 演示环境及定制
24小时咨询电话：18028750128（微信同号）18926015545（微信同号）。
## 反馈交流
- 喜欢这个基础后台的小伙伴留下你的小星星啦,star,star哦！