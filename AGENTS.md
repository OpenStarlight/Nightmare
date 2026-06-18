# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## 项目概览

Nightmare 是一个基于 Fabric 的 Minecraft 26.2 生存难度提升模组，灵感来自 MITE。项目目标是重写生存早期流程、工具/矿物等级、玩家成长、掉落、食物、世界生成和部分原版系统，使游戏整体更慢、更硬核。

这个仓库不是单纯“新增物品”的模组。很多行为是通过以下几类机制叠加实现的：

- Java 注册：新增方块、物品、工具材料、世界生成入口。
- Fabric 事件：创造栏插入、服务端 tick、玩家加入、命令注册、物品组件修改。
- Mixin：玩家属性、挖掘速度、自然回血、原版工具材料、方块掉落检测等核心行为。
- Datapack 覆盖：recipes、loot tables、advancements、tags、trades、worldgen。
- 资源文件：textures、models、items、lang。

README 中记录的运行环境：

- Minecraft 26.2
- Fabric Loader 0.19.3+
- Fabric API 0.152.1+26.2
- Cloth Config 26.2.155+
- Mod Menu 20.0.0-beta.2+（可选）
- Java 25+

## 常用命令

在仓库根目录使用 Gradle wrapper：

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

仓库未记录独立测试任务或测试源码集；主要验证命令是 `./gradlew build`。用户通常会自行构建并反馈结果，不要主动进行冗余构建验证。

开发运行通常用 `./gradlew runClient`，服务端逻辑可用 `./gradlew runServer`。如果用户反馈编译错误，优先根据错误定位具体源码/资源文件，不要做大范围重构。

## 初始化与入口流程

`NightmareMod.onInitialize()` 是主流程，当前初始化顺序大致为：

1. `ModBlocks.initialize()`：注册创造栏方块排序，并由方块注册阶段完成方块/方块物品注册。
2. `ModItems.initialize()`：注册创造栏物品/工具/食物排序。
3. `ItemModifier.initialize()`：修改原版物品组件、禁用物品、工具伤害/攻速等。
4. `BlockModifier.initialize()`：处理原版方块属性修改和额外掉落需求。
5. `ModWorldGeneration.initialize()`：注入自定义 placed feature 到主世界生物群系。
6. 注册服务端 tick、玩家加入事件和 debug 命令。

注意：很多实际注册发生在类静态字段初始化中，例如 `ModBlocks`/`ModItems` 的 `public static final` 字段。新增内容时要确认类会被初始化。

## 主要源码结构

- `src/main/java/cn/starlight/nightmare/NightmareMod.java`
  - Fabric 主初始化入口。
  - 注册 debug 命令 `/nightmare ...`。
  - 包含 `/nightmare scanOre <radius>` 的矿物扫描逻辑。

- `src/main/java/cn/starlight/nightmare/block/ModBlocks.java`
  - 注册模组方块和方块物品。
  - 处理自然方块/建筑方块创造栏排序。
  - 模组方块硬度和爆炸抗性在这里用 `blockProperties(...)` 显式定义，不应再直接 `ofFullCopy` 原版方块。

- `src/main/java/cn/starlight/nightmare/item/ModItems.java`
  - 注册材料、食物、工具。
  - 管理创造栏中 ingredients、tools、food、combat 的插入顺序。
  - 工具 `attackDamageBaseline` 不是最终伤害，最终伤害由 `1 + material.attackDamageBonus + baseline` 得到。

- `src/main/java/cn/starlight/nightmare/item/ModToolMaterials.java`
  - 定义模组工具材料数值：耐久、速度、伤害加成、附魔值、repair tag、incorrect-for-block tag。
  - `ToolMaterial` 的附魔值必须为正数，不能设为 `0`。若要让工具不可附魔，应移除物品 `ENCHANTABLE` 组件。

- `src/main/java/cn/starlight/nightmare/modifier/ItemModifier.java`
  - 通过 `DefaultItemComponentEvents.MODIFY` 修改原版和模组物品组件。
  - 负责禁用物品从创造栏/搜索栏移除。
  - 负责原版工具伤害/攻速重写。
  - 若修改工具是否可附魔、食物属性、属性组件，优先看这里。

- `src/main/java/cn/starlight/nightmare/modifier/BlockModifier.java`
  - 处理无法可靠用 datapack 表达的方块掉落需求。
  - 当前木质方块需求通过 `nightmare:needs_flint_axe`、`nightmare:needs_iron_axe` block tag + Java 检查实现。
  - 原版方块硬度/爆炸抗性修改通过 `BlockUtil.modifyBlockProperties(...)`。

- `src/main/java/cn/starlight/nightmare/util/world/BlockUtil.java`
  - 使用 accessor mixin 修改原版方块 `explosionResistance` 和 block state `destroySpeed`。

- `src/main/java/cn/starlight/nightmare/world/ModWorldGeneration.java`
  - 使用 Fabric `BiomeModifications.addFeature` 注入 `PlacedFeature`。
  - 具体生成参数在 `src/main/resources/data/nightmare/worldgen/configured_feature` 和 `placed_feature`。

- `src/main/java/cn/starlight/nightmare/mixin`
  - `block.MixinBlock`：在方块破坏后拦截掉落，用于自定义工具需求。
  - `block.MixinBlockBehaviour`：全局挖掘速度倍率与秒挖处理。
  - `block.AccessorBlockBehaviour`、`block.AccessorBlockStateBase`：给 `BlockUtil` 修改原版方块属性。
  - `item.MixinToolMaterial`：重写原版工具材料。
  - `item.MixinConsumable`、`item.MixinFoodData`：食物/自然回血相关。
  - `entity`、`player` 下 mixin 处理伤害、距离、HUD/属性等玩家系统。

新增/删除 mixin 时必须同步 `src/main/resources/nightmare.mixins.json`。

## 数据与资源目录

- `src/main/resources/data/minecraft`
  - 覆盖原版 recipe、loot table、advancement、tag、worldgen、trade 等。
  - 很多原版内容被刻意禁用或替换，不要默认当成遗漏。

- `src/main/resources/data/nightmare`
  - 模组自己的 loot table、recipe、tag、worldgen 等。
  - `tags/block/needs_*_tool.json` 和 `tags/block/incorrect_for_*_tool.json` 是工具等级系统核心。

- `src/main/resources/assets/nightmare/textures`
  - 模组贴图。

- `src/main/resources/assets/nightmare/models`
  - 物品/方块模型。

- `src/main/resources/assets/nightmare/items`
  - Minecraft 26.2 的 item definition 文件。

- `src/main/resources/assets/nightmare/lang`
  - 翻译文件。新增方块/物品不要漏掉 `zh_cn.json` 和 `en_us.json`。

Minecraft 26.2 在本项目中使用单数 datapack 目录名，例如：

- `recipe`
- `loot_table`
- `advancement`

不要改成旧版本常见的复数目录名。

## 工具等级系统

工具等级定义在 `ToolLevel.java`。当前等级体系：

- 无工具：0
- 燧石：1
- 木：2
- 金：3
- 铜/银/黑曜石/锈铁：4
- 铁：5
- 远古金属：6
- 钻石：7
- 秘银：8
- 下界合金：9
- 艾德曼：10

修改工具等级或新增工具时必须同步检查：

- `ToolLevel.java`
- `ItemUtil.getToolLevel(...)`
- `ModToolMaterials.java`
- `MixinToolMaterial.java`
- `data/minecraft/tags/item/axes.json`
- `data/minecraft/tags/item/pickaxes.json`
- `data/minecraft/tags/item/shovels.json`
- `data/minecraft/tags/item/hoes.json`
- `data/minecraft/tags/item/swords.json`
- `data/minecraft/tags/item/spears.json`
- `data/minecraft/tags/block/incorrect_for_*_tool.json`
- `data/nightmare/tags/block/incorrect_for_*_tool.json`
- `data/nightmare/tags/block/needs_*_tool.json`

工具最终攻击伤害要区分：

- `Item.Properties().axe/sword/pickaxe/...` 传入的是 baseline，不一定是最终显示伤害。
- 当前理解公式是：`最终伤害 = 1 + material.attackDamageBonus + attackDamageBaseline`。
- 修改材料 `attackDamageBonus` 后，要重新反推所有相关工具 baseline。

## 方块/工具掉落规则

方块/工具掉落规则由三层共同决定：

1. datapack block tag：
   - `minecraft:incorrect_for_*_tool`
   - `nightmare:incorrect_for_*_tool`
   - `nightmare:needs_*_tool`
2. `ToolMaterial` 的 incorrect block tag。
3. `BlockModifier` + `MixinBlock` 的破坏后掉落拦截。

优先用 datapack 表达镐类挖掘等级。木头类需求不能完全依赖 vanilla `needs_*_tool`，因为部分木质方块本身不可靠触发 `requiresCorrectToolForDrops`；这类继续走 `BlockModifier`。

当前规则中的重点：

- 铁矿/铁块等最低铜级。
- 秘银矿/秘银块最低钻石级。
- 远古残骸/下界合金块最低秘银级。
- 艾德曼矿/艾德曼块最低下界合金级。
- 黑曜石、哭泣黑曜石、重生锚最低铁级。
- 末地石、附魔台、末影箱最低铁级。
- 铁砧、炼药锅、钟最低铜级。
- 铜锁链最低金级；铁锁链最低铜级。
- 主世界木质方块最低燧石斧；地狱 crimson/warped 菌木及其衍生方块最低铁斧。

## 原版修改优先级

修改原版行为前，先查项目已有覆盖，再查原版数据。

优先级：

1. 能用 datapack 覆盖准确表达的，优先写 `src/main/resources/data/minecraft/...`。
2. datapack 不可靠或无法表达时，再考虑 Java/Fabric 事件/Mixin。
3. 已经有 modifier/mixin 管理的系统，优先延续现有入口，不要新建平行机制。

示例：

- recipe、loot table、advancement、tag、trade、worldgen：优先 datapack。
- 物品组件修改：优先 `ItemModifier`。
- 方块属性修改：优先 `BlockUtil.modifyBlockProperties(...)` 或 `ModBlocks` 自定义属性。
- 破坏后是否掉落：优先看 `BlockModifier` + `MixinBlock`。

## 反查原版/反编译时的查找顺序

需要确认原版行为、字段名、方法名、数据路径或资源格式时，优先查本地 Gradle/Fabric Loom 缓存，不要先猜。

常用原版 jar：

- `C:\Users\Stars\.gradle\caches\fabric-loom\minecraftMaven\net\minecraft\minecraft-common-deobf\26.2\minecraft-common-deobf-26.2.jar`
  - 主要查通用端 class、原版 datapack 数据、loot table、recipe、advancement、tag、worldgen。
- `C:\Users\Stars\.gradle\caches\fabric-loom\minecraftMaven\net\minecraft\minecraft-clientonly-deobf\26.2\minecraft-clientonly-deobf-26.2.jar`
  - 主要查客户端专属 class 和客户端资源。

常用 Fabric API jar：

- `C:\Users\Stars\.gradle\caches\modules-2\files-2.1\net.fabricmc.fabric-api\fabric-api\0.152.1+26.2\9a01ffe3dade4afd3a085fcc0ed59afc24a17f7b\fabric-api-0.152.1+26.2-sources.jar`
  - 优先查这个 sources jar，适合确认 Fabric API 事件、回调接口、方法签名和用法。
- `C:\Users\Stars\.gradle\caches\modules-2\files-2.1\net.fabricmc.fabric-api\fabric-api\0.152.1+26.2\8c5317d4ba22dcc5def1c691c346595769ba3304\fabric-api-0.152.1+26.2.jar`
  - 没有源码需求时可用于 `javap` 或确认 class 是否存在。

查原版 datapack/资源时优先从 jar 内这些路径找：

- `data/minecraft/recipe/...`
- `data/minecraft/loot_table/...`
- `data/minecraft/advancement/...`
- `data/minecraft/tags/...`
- `data/minecraft/worldgen/...`
- `assets/minecraft/textures/...`
- `assets/minecraft/models/...`
- `assets/minecraft/items/...`
- `assets/minecraft/blockstates/...`
- `assets/minecraft/lang/...`

查 Java 方法名、字段名、类结构时可用 `javap` 看 deobf jar，例如：

```bash
javap -classpath "C:\Users\Stars\.gradle\caches\fabric-loom\minecraftMaven\net\minecraft\minecraft-common-deobf\26.2\minecraft-common-deobf-26.2.jar" -private net.minecraft.world.level.block.state.BlockBehaviour
```

如果要确认某个原版 JSON 是否存在或某个 ID 是否在原版数据中出现，可以用只读脚本遍历 jar。只做查询时不要改 jar，也不要把原版资源解包后直接当项目文件编辑；需要覆盖时在 `src/main/resources/data/minecraft` 或 `src/main/resources/assets/minecraft` 下创建同路径文件。

常见查询目标：

- 原版方块/物品 tag：查 `data/minecraft/tags/block`、`data/minecraft/tags/item`。
- 原版矿物掉落：查 `data/minecraft/loot_table/blocks/*ore*.json`。
- 原版配方禁用/替换：查 `data/minecraft/recipe` 和对应 `data/minecraft/advancement/recipes`。
- 原版世界生成：查 `data/minecraft/worldgen/configured_feature`、`placed_feature`、`noise_settings` 等。
- 原版贴图模板：查 `assets/minecraft/textures/block` 和 `assets/minecraft/textures/item`。
- 原版模型/item definition：查 `assets/minecraft/models`、`assets/minecraft/items`、`assets/minecraft/blockstates`。

查找顺序建议：

1. 先查项目当前是否已有覆盖或 mixin。
2. 再查原版 jar 中对应 datapack/resource/class。
3. 最后决定是补 datapack 覆盖、改 Java modifier，还是加 mixin/accessor。

## 世界生成

模组世界生成有两部分：

- Java 注入：`ModWorldGeneration.initialize()` 把 placed feature 加入主世界地下矿物阶段。
- JSON 参数：`data/nightmare/worldgen/configured_feature` 与 `placed_feature` 定义矿物大小、替换目标、高度、次数、稀有度等。

原版矿物生成也通过 `data/minecraft/worldgen` 覆盖调整。改矿物生成时要同时考虑：

- configured feature 的矿脉大小和 discard chance。
- placed feature 的 count/rarity/height range。
- 是否需要在 `/nightmare scanOre <radius>` 中统计。
- 是否需要更新 Xray 资源包（如果用户明确要求）。

## Debug 命令

`NightmareMod.debug` 为 true 时注册：

- `/nightmare scanOre <radius>`：统计玩家周围已加载区块中的矿物数量，不强制加载/生成区块。
- `/nightmare field disableBreakSpeedModifier <true|false>`
- `/nightmare field disableToolRequirement <true|false>`

这些命令用于在开发世界中验证矿物分布、挖掘速度和掉落需求。

## ALL_EDITS.md 记录规则

`ALL_EDITS.md` 是玩法改动总览，不是 commit log，也不是实现细节记录。

**每次做出影响游戏的调整时，必须在 `ALL_EDITS.md` 中记录。** 记录风格保持简洁，仿照已有条目（例如「降低生成量」，不写数值），让读者了解改了什么即可。如果同一条目后续调整了数值，不重复记录。不写技术细节，不写「再次更改」这类词语，多次修改只保留最终状态的描述。

应记录：

- 新增玩法内容。
- 原版机制重大修改。
- 工具等级、矿物生成、掉落规则、玩家系统等对玩家可见的大改动。

不应记录：

- 临时实现细节。
- 代码内部重构。
- 用户明确说”不需要写入记录”的改动。
- 可以直接从代码/资源文件看出来的低层细节，除非它影响玩法理解。

## 贴图修改要求

用户对贴图质量要求较高，不接受低版本风格、随手涂色、形状脏乱或批量粗糙生成。贴图相关任务要先理解风格再动手，不要靠随机像素或简单色相替换糊弄。

### 方块/矿石贴图

- 方块/矿石贴图要参考原版高版本贴图的形状、明暗和细节，不要用低版本 MITE 原图直接缩放或照搬。
- 矿石贴图要严格区分石头/深板岩底材和矿物像素，不能让矿物颜色污染到底材，也不能残留其它矿物纹理。
- 金属矿如果产物是锭，矿石和金属块风格应更接近铁/金，而不是钻石/绿宝石类宝石风格。
- 远古残骸类方块要注意侧面和上下纹理不同；不要把六面都做成同一张。
- 金属块要参考原版金属块结构和已有锭配色，不能只是纯色噪声。

### 原矿/碎片贴图

- 原矿贴图优先参考原版粗铁/粗金/粗铜的轮廓和明暗，不要画成不连贯的散点。
- 如果用户指定“银用粗铁、秘银用粗金”，不要再另造轮廓。
- 碎片贴图要轮廓连通、像素干净，避免多出孤立像素或异常连接。
- 艾德曼碎片曾指定使用海晶碎片的形状再重配色。

### 工具贴图

- 工具贴图必须使用现代原版工具风格：有高版本高光、边缘和材质层次，不要做成低版本平涂。
- 推荐流程：以对应的原版 `iron_<tool>.png` 为基础，对比 iron/golden/diamond/copper 同工具贴图找出不变的木柄像素，保留木柄，只重映射工具头/刃部颜色与亮度。
- 不要用下界合金工具当普通金属工具模板，因为下界合金工具柄本来就不是木柄。
- 材料配色要参考本项目已有锭/材料贴图；银、秘银、远古金属、艾德曼等不要随意压暗或漂白。
- 黑曜石工具应呈黑中泛紫的感觉，并保留适度内部高光，不能变白或完全无高光。
- 锈铁工具应接近 MITE 原铁颜色并略微泛黄。
- 批量生成贴图前，先做单个样例给用户验收；用户认可后再拓展到同系列全部贴图。
- 用户明确要求“一个一个认真搞”时，不要用粗糙批量脚本直接覆盖全部贴图。

## 工作方式偏好

- 回答和说明使用中文。
- 不需要每次完成后主动构建；用户通常会自行构建并反馈。
- 修改普通源码/文本时使用编辑工具，不要用 Python/PowerShell 之类外部脚本改文件。
- 源码注释保持最少。现有注释多为简短中文段落说明。
- 修改原版行为前先查已有覆盖，再查原版数据，确认覆盖完整。
- 对 UI/资源/贴图这类主观质量任务，不要一次性大批量假定正确；先给用户验收样例。
