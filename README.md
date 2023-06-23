# KAI API开放平台

## 项目简介：

用于用户可调用接口的在线开放api平台,也可供开发者提供提供接口调用（仅限Java用户）。本项目仅适用于学习。

由于服务器内存原因，暂时上不了线。

## 技术选型:

* 后端技术：
    - springboot
    - MyBatis-Plus 及 MyBatis X 自动生成
    - Feign 进行各个微服务之间的远程调用
    - Spring Security +jwt 实现单点登录
    - Rabbit Mq 消息队列
    - Spring Cloud Gateway 微服务网关
    - Dubbo 分布式（RPC）各微服务之间远程调用。
    - nacos 作为微服务的注册中心
    - 阿里云短信，支付宝支付api
    - mysql
    - redis
    - Spring Boot Starter（SDK 开发）
    - Swagger + Knife4j 接口文档生成
    - Hutool、Apache Common Utils、Gson 等工具库

- 前端技术
    - React 18
    - Ant Design Pro 5.x 脚手架
    - Ant Design & Procomponents 组件库
    - Umi 4 前端框架
    - OpenAPI 前端代码生成

## 模块介绍

* kaiapi：用户以及接口管理等功能，为项目核心模块
* kaiapi-client-sdk：供开发者调用的sdk。为了开发者更好的调用接口
* kaiapi-common：项目的通用模块，包括通用的返回、异常、常量、实体等
* kaiapi-gateway：项目网关，所有请求都经过网关，由网关进行统一的鉴权
* kaiapi-interface：提供可调用的接口服务
* kaiapi-order：订单服务，提供生成订单、取消订单等功能
* kaiapi-thrid-part：第三方服务，用于阿里云短信的发送，支付宝沙箱支付。

## 演示图
用户名密码登录：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/e4c657b5-268c-4623-9124-1c1ffba0865a)
手机号登录：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/8e5f602a-3bc0-4f4d-bace-0b5cafecb9d3)
注册：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/c516b53d-52ee-4c63-9e6d-b3012afec7df)
首页：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/6a715244-e511-4b00-bfe0-c676f10c6aa8)
接口调用演示：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/14753904-e995-4632-b124-ec9c86d3f6b5)
接口购买：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/21ea58f6-f155-455f-814e-abe8075caf05)
待支付页面：![image](https://github.com/lklove/kaiapi-backend/assets/82987840/67d9be7d-57dd-463a-91f4-6d0b48995b8c)





