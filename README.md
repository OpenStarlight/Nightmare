# Nightmare

Nightmare 是一个基于 Fabric 的 Minecraft 生存向改造 Mod，目标是降低原版前期资源获取速度、提高生存压力，并重新调整玩家成长、工具体系、世界生成与部分基础机制。

## 主要内容

- 新增燧石阶段工具与材料：燧石碎片、皮革线、燧石斧、燧石手斧、燧石小刀、燧石铲
- 重做部分基础资源获取：砂砾掉落燧石碎片，部分草类掉落小麦种子
- 调整工具等级与原版工具获取：移除或替换部分木质、石质工具来源
- 调整方块采集规则：部分木质方块需要斧类工具并满足最低工具等级
- 调整玩家系统：经验升级曲线、饥饿与回血、伤害、挖掘速度、交互距离、等级成长加成
- 调整世界生成：村庄生成量降低，黄花生成改为偏多但不密集的团簇式分布
- 调整交易、战利品表、配方、进度与物品标签，配合整体生存节奏

## 运行环境

- Minecraft：26.1.2
- Fabric Loader：0.19.3 或更高
- Fabric API：0.151.0+26.1.2 或兼容版本
- Java：25 或更高
- 客户端与服务端均需要安装

## 开发环境

- Gradle
- Fabric Loom：1.16-SNAPSHOT
- Java 25
- 推荐使用 IntelliJ IDEA 或其他支持 Gradle/Fabric 的 IDE

常用命令：

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

Windows 环境可使用：

```bash
gradlew.bat build
gradlew.bat runClient
gradlew.bat runServer
```

## 目录说明

- `src/main/java`：通用逻辑、注册、Mixin、服务端逻辑
- `src/client/java`：客户端入口与客户端 Mixin
- `src/main/resources/data`：配方、战利品表、标签、世界生成、交易、进度等数据覆盖
- `src/main/resources/assets`：物品模型、语言文件等资源
- `ALL_EDITS.md`：重要玩法与世界生成改动记录

## 许可证

本项目使用 GNU General Public License v3.0，详见 `LICENSE`
