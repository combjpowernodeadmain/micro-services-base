# 项目开发说明

## 一、分支开发说明

当前项目中主要有两个分支，一为develop分支，二为master分支。

* develop分支主要开发分支，作为内网测试使用。
* master分支为外网部署分支，主要是内网测试通过后将develop分支合并到master分支。

### 1.1 功能开发分支

每次开发新功能时，基于develop分支检出以feature_为前缀的分支。操作命令如下：

```sh
# 确保当前在develop分支
git checkout develop

# 检出功能开发分支
git checkout -b feature_<功能分支>

# 提交功能代码
git add .
git commit -m "功能描述"

# 推送代码
git push origin feature_<功能分支>
```

功能分支代码提交后，需要在gitee.com上提交代码合并请求pull request。


### 1.2 问题修复分支

每次修复线上问题时，基于master分支检出以issue_为前缀的分支。操作命令如下：

```sh
# 确保当前在master分支
git checkout master

# 检出问题修复分支
git checkout -b issue_<修复功能描述>

# 提交修复代码
git add .
git commit -m "描述"

# 推送代码
git push origin issue_<修复功能描述>
```

修复分支代码提交后，需要在gitee.com上提交代码合并请求pull request。