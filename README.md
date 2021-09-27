### MK-OPManager  
___
__该项目在项目测试中__
___
### 概述  
一个管理OP的插件  
___理论上全版本可用___  

### 命令相关  
/mkom addTempOP [玩家名] [密码] [天 _（可填0）_ ] [时 _（可填0）_ ] [分 _（不可填0）_ ] [秒 _（可填0）_ ]   
添加一个临时管理员

/mkom addOP [玩家名] [密码]                            
添加一个管理员

/mkom addCommand [命令] [密码]    
添加一个命令，该命令会被禁止执行 __（除超级管理员可以执行外）__    

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

