# Geek OJ 在线代码测评
Geek OJ 后端仓库
## 项目简介
基于 Spring Cloud Alibaba 微服务架构 + 消息队列 + Docker 容器化技术的在线代码测评平台。

在系统前台，管理员可以发布、管理题目和题解，用户可以自由搜索、阅读题目、编写代码，可以在线自测并提交代码。

在系统后端，可根据管理员设置的运行限制和测试用例，基于自研代码沙箱对用户的代码隔离并完成编译、执行并输出测评结果。
## 技术栈
Spring Boot, Spring Cloud, MySQL, Redis, Mybatis-Plus, SaToken, Nacos, Dubbo, Sentinel, RocketMQ, Docker
## 相关链接
* Geek OJ 前端仓库：https://github.com/yutianzeabc/GeekOJ-Cloud-Frontend
* Geek OJ 代码沙箱：https://github.com/yutianzeabc/GeekOJ-Code-Sandbox
