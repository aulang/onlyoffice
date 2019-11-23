## onlyoffice
### 1. 安装文档服务器
```shell script
docker pull onlyoffice/documentserver

docker run -d -p 8088:80 --name=documentserver --restart=always -v /f/onlyoffice/logs:/var/log/onlyoffice -v /f/onlyoffice/data:/var/www/onlyoffice/Data -v /f/onlyoffice/lib:/var/lib/onlyoffice -v /f/onlyoffice/db:/var/lib/postgresql onlyoffice/documentserver
```

### 2. 安装中文字体
1. 准备Windows下中文字体文件打包好  
C:\Windows\Fonts --> 设计用于（中文）--> 选择复制到/f/onlyoffice/data/winfonts

2. 安装Windows中文字体  
复制/var/www/onlyoffice/Data/winfonts到/usr/share/fonts   
执行documentserver-generate-allfonts.sh

```shell script
# 进入容器
docker exec -it documentserver /bin/bash

# 在容器中执行命令
/# mv /var/www/onlyoffice/Data/winfonts/ usr/share/fonts/
/# cd /usr/bin
/# documentserver-generate-allfonts.sh
/# exit
```

### 3. 配置安全令牌
1. 进入容器修改文件/etc/onlyoffice/documentserver/local.json
    ```json
    {
      "services": {
        "CoAuthoring": {
          "sql": {
            "dbHost": "localhost",
            "dbName": "onlyoffice",
            "dbUser": "onlyoffice",
            "dbPass": "onlyoffice"
          },
          "redis": {
            "host": "localhost"
          },
          "token": {
            "enable": {
              "request": {
                "inbox": false,
                "outbox": false
              },
              "browser": false
            },
            "inbox": {
              "header": "Authorization"
            },
            "outbox": {
              "header": "Authorization"
            }
          },
          "secret": {
            "inbox": {
              "string": "secret"
            },
            "outbox": {
              "string": "secret"
            },
            "session": {
              "string": "secret"
            }
          }
        }
      },
      "rabbitmq": {
        "url": "amqp://guest:guest@localhost"
      }
    }
    ```
    修改为
    ```json
    {
      "services": {
        "CoAuthoring": {
          "sql": {
            "dbHost": "localhost",
            "dbName": "onlyoffice",
            "dbUser": "onlyoffice",
            "dbPass": "onlyoffice"
          },
          "redis": {
            "host": "localhost"
          },
          "token": {
            "enable": {
              "request": {
                "inbox": true,
                "outbox": true
              },
              "browser": true
            },
            "inbox": {
              "header": "Authorization"
            },
            "outbox": {
              "header": "Authorization"
            }
          },
          "secret": {
            "inbox": {
              "string": "PLr15GPwJ8lzqlnk7DA7"
            },
            "outbox": {
              "string": "PLr15GPwJ8lzqlnk7DA7"
            },
            "session": {
              "string": "PLr15GPwJ8lzqlnk7DA7"
            }
          }
        }
      },
      "rabbitmq": {
        "url": "amqp://guest:guest@localhost"
      }
    }
    ```
2. 重启文档服务器
    ```shell script
    supervisorctl restart all
    ```