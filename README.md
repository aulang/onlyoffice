## onlyoffice
### 1. 安装文档服务器
```shell script
docker pull onlyoffice/documentserver

docker run -d \
-v /data/onlyoffice/logs:/var/log/onlyoffice \
-v /data/onlyoffice/data:/var/www/onlyoffice/Data \
-v /data/onlyoffice/lib:/var/lib/onlyoffice \
-v /data/onlyoffice/db:/var/lib/postgresql \
-p 8088:80 \
--restart=always \
--name=documentserver \
onlyoffice/documentserver
```

### 2. 安装中文字体
1. 准备Windows下中文字体文件打包好  
C:\Windows\Fonts --> 设计用于（中文）--> 选择复制到/data/onlyoffice/data/WinFonts

2. 安装Windows中文字体  
复制/var/www/onlyoffice/Data/WinFonts到/usr/share/fonts   
执行documentserver-generate-allfonts.sh

```shell script
# 进入容器
docker exec -it documentserver /bin/bash

# 在容器中执行命令
/# mv /var/www/onlyoffice/Data/WinFonts/ usr/share/fonts/
/# cd /usr/bin
/# documentserver-generate-allfonts.sh
/# exit
```

3. 字体中文名显示  
参见字体修改[字体](./fonts/README.md)

### 3. 配置安全令牌
1. 配置文件路径/etc/onlyoffice/documentserver/local.json
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
    修改内容为
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
              "string": "密钥字符串"
            },
            "outbox": {
              "string": "密钥字符串"
            },
            "session": {
              "string": "密钥字符串"
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

### 破解连接数限制
进度容器，修改如下文件
  - /var/www/onlyoffice/documentserver/web-apps/apps/documenteditor/main/app.js
  - /var/www/onlyoffice/documentserver/web-apps/apps/documenteditor/mobile/app.js
  - /var/www/onlyoffice/documentserver/web-apps/apps/presentationeditor/main/app.js
  - /var/www/onlyoffice/documentserver/web-apps/apps/presentationeditor/mobile/app.js
  - /var/www/onlyoffice/documentserver/web-apps/apps/spreadsheeteditor/main/app.js
  - /var/www/onlyoffice/documentserver/web-apps/apps/presentationeditor/mobile/app.js
  
  修改this._state.licenseType=(t或e)为this._state.licenseType=0