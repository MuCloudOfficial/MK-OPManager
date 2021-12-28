### MK-OPManager  
___
__该项目重构完毕 启动测试中__  
___
### 概述  
一个管理OP的插件  
防止服内卡OP等操作  
___理论上全版本可用___  

### 命令相关  
/mkom addTempOP [玩家] [密码] [时间]  
添加一个临时管理员  
Tip: 时间格式 -> X日X时X分 -> 3 2 1 即为 给予玩家 3日2时1分 的临时管理员权限

/mkom addOP [玩家] [密码]  
添加一个管理员  

/mkom delOP [玩家] [密码]  
移除一个管理员  

/mkom addCommand [命令] [密码]  
添加一个命令,它将会禁止被所有玩家执行(除超级管理外)  

/mkom delCommand [命令] [密码]  
从禁止命令中清除该命令  

/mkom listOP  
列出管理员名单  

/mkom reload  
重载插件  

### 权限相关  
mkopmanager.admin 全局管理权限  
mkopmanager.addOP 添加管理员权限  
mkopmanager.addTempOP 添加临时管理员权限  
mkopmanager.addCommands 添加禁止命令权限  

### 配置相关
__Superadministrators__  
超级管理员  
拥有全部的管理员权限  

__WhiteList__  
具有白名单的玩家，这些玩家将拥有管理员身份  
权利可能比超级管理员更低，这取决于 BannedCommands 中定义的禁止命令列表.  

__TempWhiteList__  
临时管理员列表  
在该列中的玩家可以临时拥有管理员权利  
权利与管理员相同  

__BannedCommands__  
在该列中的命令只能由超级管理员发出.  
用于限制管理员权利的方法 __（可能是唯一方法）__  
Tip: 最近有个关于 MultiVerse Core 的命令正则表达式BUG可导致卡服的操作，可以使用该特性禁止（但不会确定最终效果）

