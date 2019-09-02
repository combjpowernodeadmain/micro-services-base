# 工作流开发说明

## 使用说明
1.在业务系统pom中添加以下依赖
```
<!--工作流依赖-->
<dependency>
    <groupId>com.bjzhianjia.scp</groupId>
    <artifactId>scp-workflow-base</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2.执行工作流脚本   
[scp_workflow(下属模式).sql](https://gitee.com/lutuo/dev_rules/blob/develop/deploy/templet/zhzl/db/scp_workflow(%E4%B8%8B%E5%B1%9E%E6%A8%A1%E5%BC%8F).sql)   
3.在业务系统xxxxx.yml配置文件添加工作流数据库连接方式
```
    # 工作流数据源地址
    WORKFLOW.MYSQL_HOST=192.168.199.2
    WORKFLOW.MYSQL_PORT=3306
    WORKFLOW.MYSQL_USER_NAME=root
    WORKFLOW.MYSQL_USER_PASS=123456
```
4.API帮助手册   
[工作流接口规范.docx](https://gitee.com/lutuo/dev_rules/blob/develop/docs/%E5%B7%A5%E4%BD%9C%E6%B5%81%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83.docx)


## 获取用户信息代码
获取登录用户信息的基类是：`import com.bjzhianjia.scp.core.context.BaseContextHandler`

### 获取登录用户信息

```java
// 登录用户Id
BaseContextHandler.getUserID()

// 登录用户名
BaseContextHandler.getUsername()

// 获取当前用户的租户Id
BaseContextHandler.getTenantID()

// 获取用户登录token
BaseContextHandler.getToken()

// 获取部门ID
BaseContextHandler.getDepartID()


// 获取登录用户名字
BaseContextHandler.getName()
```

### 获取登录用户组织列表

```java
@Autowired
@Lazy
IUserFeign userFeign;


// 获取当前人拥有的流程岗位
List<String> userFlowPositions = userFeign.getUserFlowPositions(BaseContextHandler.getUserID());
```

此操作涉及到的SQL为：

```sql

select p.* from base_position p
  inner join base_position_user bpu
on bpu.position_id = p.id
 where type = 'flow' AND bpu.user_id = #{userId}
```


### 获取登录用户角色列表

```java
@Autowired
@Lazy
IUserFeign userFeign;


// 获取当前人拥有的流程岗位
List<String> userFlowPositions = userFeign.getUserFlowPositions(BaseContextHandler.getUserID());
```