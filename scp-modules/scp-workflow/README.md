# 工作流开发组织机构(Deprecated)
暂时弃用该服务，采用scp-workflow-base模块集成在业务系统中

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