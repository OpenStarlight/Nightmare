# Nightmare

Nightmare 是一个基于 Fabric 的提高 Minecraft 生存难度的模组。这个模组尝试重新定义和调整了游戏的各项机制并增加了大量新内容，以大幅增加游戏的难度。  
本模组的灵感来源于早些年大火的 [贝爷生存 (MITE, Minecraft Is Too Easy)](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1294284-minecraft-is-too-easy-mite-mod) 改版，并继承了原改版的大量机制和内容，并基于其进行拓展。  
原改版在 2017 年就已经停更，因此 Nightmare 想将这个传奇之作重新带回大众视野，并继续进行更新。非常感谢改版原作者 [Avernite](https://www.minecraftforum.net/members/Avernite) 的创意与为之付出的汗水。

## 主要内容

模组目前处于初期开发阶段，暂未有一个系统性的玩法介绍，请检查 [ALL_EDITS.md](ALL_EDITS.md) 以了解这个模组更改了什么。

## 运行环境

- Minecraft 26.2
- Fabric Loader 0.19.3 或更高
- Fabric API 0.152.1+26.2 或更高
- Cloth Config 26.2.155 或更高
- Mod Menu 20.0.0-beta.2 或更高 (可选)
- Java 25 或更高
- 适配客户端与服务端，双端均需要安装

## 开发环境

- Gradle
- Fabric Loom 1.16-SNAPSHOT
- Java 25
- IntelliJ IDEA (推荐)

## 目录说明

- `src/main`：通用逻辑、注册、Mixin、服务端逻辑，通用资源
- `src/client`：客户端入口、Mixin、资源

## 兼容性提醒

由于 Fabric API 功能有限，本模组大量使用 Mixin 对原版的机制进行了修改，这意味着本模组并不期望与任何其他未测试的模组兼容。
因此当您添加其他模组时，冲突和崩溃是在预期之内的。如果您遇到了这个情况，请向开发者反馈，我们会酌情处理。

### 已知兼容模组
- 几乎所有优化模组
- 几乎所有信息显示模组

### 已知不兼容模组
- 无

## 许可证

本项目使用 GNU General Public License v3.0 (GPL v3.0) 开源和发行，详见 [LICENSE](LICENSE) 文件。  
本项目归属于 [Starlight](https://github.com/Starlight) 团队。