# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

車のメンテナンス記録を管理するAndroidアプリ。Jetpack Compose + Kotlin + MVVMアーキテクチャで構成された、MVP（最小実行可能製品）段階のプロジェクトです。

## 開発コマンド

### ビルドとテスト
```bash
# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# ユニットテスト実行
./gradlew testDebugUnitTest

# Instrumented テスト実行
./gradlew connectedAndroidTest

# 全テスト実行
./gradlew test
```

### コード品質チェック
```bash
# Kotlinコンパイル
./gradlew compileDebugKotlin

# Lintチェック
./gradlew lintDebug
```

## アーキテクチャ

### 技術スタック
- **言語**: Kotlin 100%
- **UI**: Jetpack Compose + Material 3
- **アーキテクチャ**: MVVM + 緩めのClean Architecture
- **状態管理**: ViewModel + StateFlow/MutableStateFlow
- **データベース**: Room（予定）
- **DI**: Hilt（推奨）

### パッケージ構成
```
to.msn.wings.carmaintenancerecord
├─ ui/          # Compose UI / ViewModel / UiState
│  ├─ home/
│  ├─ car/
│  └─ maintenance/
├─ domain/      # ビジネスロジック
│  ├─ model/
│  └─ usecase/
├─ data/        # Repository / Room / Mapper
│  ├─ repository/
│  ├─ local/
│  │  ├─ dao/
│  │  ├─ entity/
│  │  └─ AppDatabase.kt
│  └─ mapper/
└─ di/          # Hilt Module
```

## MVP機能要件

### 実装予定機能
1. **車両管理**: 1台の車の登録・走行距離更新
2. **メンテナンス履歴**: 記録の登録・一覧表示
3. **次回メンテナンス表示**: 残り距離/日数の計算表示
4. **ホーム画面**: 概要情報の表示

### 依存関係の流れ
```
ViewModel → UseCase → Repository → Room
```

## 開発ガイドライン

### コード規約
- Kotlinコーディング規約に準拠
- Composeコンポーネントは関数名をPascalCaseで記述
- ViewModelは画面単位のUiStateを管理
- 将来のKMP移行を見据えたDomain/Dataレイヤの分離

### テスト方針（MVP段階）
- UI: 手動確認
- Domain: UseCase単体テスト（任意）
- Data: DAOテスト（必要に応じて）

### 環境要件
- Android Studio Hedgehog 以降
- minSdk = 26
- targetSdk = 最新
- Compose Compiler Version = 1.5.1