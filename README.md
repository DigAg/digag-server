![digag-server](https://img.shields.io/github/tag/DigAg/digag-server.svg) ![License MIT](https://img.shields.io/badge/license-MIT-blue.svg) [![Build Status](https://api.travis-ci.org/DigAg/digag-server.svg?branch=master)](https://travis-ci.org/DigAg/digag-server) [![](https://img.shields.io/docker/stars/digag/digag-server.svg)](https://hub.docker.com/r/digag/digag-server 'DockerHub') [![](https://img.shields.io/docker/pulls/digag/digag-server.svg)](https://hub.docker.com/r/digag/digag-server 'DockerHub')


 
 ### Usage

To run it:

     docker-compose up -d
     # 注意在mysql容器里先创建数据库，不然访问时会导致web容器关闭
     docker-compose exec mysql bash
     mysql -uroot -proot
     create database db_digag;
