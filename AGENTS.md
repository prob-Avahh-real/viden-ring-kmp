# viden-ring-kmp

Viden Ring — KMP An elemental ring knowledge/sharing app built with Kotlin Multiplatform + Compose Multiplatform. Master 8 elements (Fire, Water, Earth, Air, Light, Shadow, Arcane, Nature) on a mystical ring interface. Tap segments to view details, level up abilities, and track your progress.

## 用途与 Target 判定

Kotlin Multiplatform / Compose 工程（已归档）

- 移动端：需要
- 智能硬件：可选（八元素圆环 Canvas 交互，未接外部穿戴硬件）

## 目标平台

| 平台 | 方式 | 产出 |
|------|------|------|
| 移动端 | KMP | APK / 既有 iOS |
| 智能硬件 | 可选 | 暂未接入外部硬件 |

## 技术栈信号

- 检测到：kmp

## 命令

```bash
./gradlew :composeApp:assembleDebug
```

## 状态

已归档。默认只做必要修复与知识库同步，不新开大范围需求。

## 助手约束

- Loop：先定可验证 Done 与停止条件；每切片过验证环；失败回灌；详见 docs/LOOP_ENGINEERING.md
- Harness：遵循根 AGENTS「Harness Engineering」；Guide=本文件，Sensor=测试/CI；同类失误二次须沉淀规则或测试（棘轮）。详见 docs/HARNESS_ENGINEERING.md
- 遵循仓库根 `/Users/skat/AGENTS.md` 与本文件的用途判定
- 可选 Target 可搁置；不适用形态不要硬加
- 改动聚焦；公共逻辑抽离复用
