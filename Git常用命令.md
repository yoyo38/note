# Git常用命令

Git 常用命令可以按使用场景分类，我帮你梳理了最核心的一组：

## 一、配置与初始化

| 命令                                       | 说明                   |
| ------------------------------------------ | ---------------------- |
| `git config --global user.name "你的名字"` | 设置用户名             |
| `git config --global user.email "邮箱"`    | 设置邮箱               |
| `git init`                                 | 在当前目录初始化新仓库 |
| `git clone <仓库地址>`                     | 克隆远程仓库到本地     |

---

## 二、日常开发（核心流程）

| 命令                       | 说明                               |
| -------------------------- | ---------------------------------- |
| `git status`               | 查看当前文件状态（最常用）         |
| `git add <文件名>`         | 将文件添加到暂存区                 |
| `git add .`                | 添加所有变更到暂存区               |
| `git commit -m "提交信息"` | 提交暂存区内容到本地仓库           |
| `git commit -am "信息"`    | 跳过 add，直接提交已跟踪文件的修改 |
| `git log`                  | 查看提交历史                       |
| `git log --oneline`        | 简洁版历史记录                     |

---

## 三、分支管理

| 命令                       | 说明                         |
| -------------------------- | ---------------------------- |
| `git branch`               | 查看所有分支（当前分支带 *） |
| `git branch <分支名>`      | 创建新分支                   |
| `git checkout <分支名>`    | 切换分支                     |
| `git checkout -b <分支名>` | 创建并切换到新分支           |
| `git switch <分支名>`      | 切换分支（新版推荐）         |
| `git switch -c <分支名>`   | 创建并切换（新版推荐）       |
| `git merge <分支名>`       | 将指定分支合并到当前分支     |
| `git branch -d <分支名>`   | 删除本地分支                 |
| `git branch -D <分支名>`   | 强制删除未合并的分支         |

---

## 四、远程仓库

| 命令                           | 说明                                  |
| ------------------------------ | ------------------------------------- |
| `git remote -v`                | 查看远程仓库地址                      |
| `git remote add origin <地址>` | 添加远程仓库                          |
| `git push origin <分支名>`     | 推送到远程                            |
| `git push -u origin <分支名>`  | 推送并建立关联（首次用）              |
| `git pull`                     | 拉取远程更新并合并（= fetch + merge） |
| `git fetch`                    | 只拉取远程更新，不合并                |
| `git clone <地址>`             | 克隆远程仓库                          |

---

## 五、撤销与回退

| 命令                            | 说明                                  |
| ------------------------------- | ------------------------------------- |
| `git restore <文件名>`          | 丢弃工作区的修改（未 add）            |
| `git restore --staged <文件名>` | 将文件移出暂存区（unstage）           |
| `git reset HEAD <文件名>`       | 同上，旧版写法                        |
| `git reset --soft HEAD~1`       | 撤销 commit，保留修改在暂存区         |
| `git reset --mixed HEAD~1`      | 撤销 commit，保留修改在工作区（默认） |
| `git reset --hard HEAD~1`       | ⚠️ 撤销 commit，丢弃所有修改（危险）   |
| `git revert <commit-id>`        | 用新 commit 撤销指定提交（安全）      |
| `git stash`                     | 暂存当前未提交的修改                  |
| `git stash pop`                 | 恢复最近一次 stash                    |

---

## 六、查看与比较

| 命令                   | 说明                               |
| ---------------------- | ---------------------------------- |
| `git diff`             | 查看工作区与暂存区的差异           |
| `git diff --staged`    | 查看暂存区与最后一次 commit 的差异 |
| `git show <commit-id>` | 查看某次提交的详情                 |
| `git log --graph`      | 图形化查看分支历史                 |

---

## 七、常用组合技巧

```bash
# 提交所有修改并推送
git add . && git commit -m "更新" && git push

# 拉取最新代码（推荐用 rebase 保持历史整洁）
git pull --rebase

# 修改最后一次提交信息
git commit --amend -m "新信息"

# 查看简洁状态
git status -sb
```

---

## 💡 新手建议

1. **不确定时先用 `git status` 和 `git diff` 查看状态**
2. **`reset --hard` 非常危险，数据会丢失**
3. **想撤销已推送的提交，用 `git revert` 而不是 reset**
4. **养成 `git pull` 前先 `git status` 的习惯**

如果你想要一份**更详细的带场景示例的版本**，或者需要 **Git 速查表（Cheat Sheet）**，我也可以帮你整理，告诉我就好 😊