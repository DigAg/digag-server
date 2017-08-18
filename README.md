![digag-server](https://img.shields.io/github/downloads/DigAg/digag-server/latest/total.svg) ![License MIT](https://img.shields.io/badge/license-MIT-blue.svg) [![Build Status](https://api.travis-ci.org/DigAg/digag-server.svg?branch=master)](https://travis-ci.org/DigAg/digag-server) [![](https://img.shields.io/docker/stars/digag/digag-server.svg)](https://hub.docker.com/r/digag/digag-server 'DockerHub') [![](https://img.shields.io/docker/pulls/digag/digag-server.svg)](https://hub.docker.com/r/digag/digag-server 'DockerHub')


 
 ### Usage
 镜像地址
```
https://dev.aliyun.com/detail.html?spm=5176.1972343.2.8.1GqX4K&repoId=54838
```

To run it:
```
     # 在本地生成镜像,需安装docker
     mvn clean package docker:build
     # 将docker-compose.yml中的 web 容器镜像改为上面生成的镜像
     docker-compose up -d
     # 注意在mysql容器里先创建数据库，不然访问时会导致web容器关闭
     docker-compose exec mysql bash
     mysql -uroot -proot
     create database `db_digag` default character set utf8 collate utf8_general_ci;
```

     
 ### online http://139.224.135.86:8080/swagger-ui.html
