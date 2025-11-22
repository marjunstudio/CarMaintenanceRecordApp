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

#### コメントの記載方針
- **コメントは最小限に**：コードから明らかな処理には書かない
- **WHATではなくWHYを書く**：「何をしているか」ではなく「なぜそうしているか」を説明
- **コメントが必要な場合**：
  - 複雑なビジネスロジックや計算式
  - 非自明な回避策やハック
  - 外部仕様やAPIの制約による実装
  - パフォーマンス最適化の理由
- **コメント不要な場合**：
  - 変数の代入や単純な処理
  - 関数名・変数名で意図が明確な処理
  - 標準的なデザインパターンの実装
- **例**：
  ```kotlin
  // ❌ 悪い例：コードを読めば分かる
  // carNameを空文字にする
  val carName = ""

  // ❌ 悪い例：WHATを説明している
  // データベースから車を取得
  carRepository.getCar(id)

  // ✅ 良い例：WHYを説明している
  // 納車時より少ない走行距離は物理的にあり得ないため拒否
  if (mileage < initialMileage) {
      return error("Invalid mileage")
  }

  // ✅ 良い例：外部制約を説明
  // Room 2.6.1のバグ回避のため、fallbackToDestructiveMigrationを使用
  // https://issuetracker.google.com/issues/xxxxx
  .fallbackToDestructiveMigration()
  ```

#### Domain層のドキュメント
- Domainモデルクラスには必ずKDocを記載する
- クラスの説明とすべてのプロパティの説明を`@property`タグで記載する
- 例:
  ```kotlin
  /**
   * 車両情報を表すドメインモデル
   * @property id 車両ID
   * @property name 車両名
   * @property mileage 現在の走行距離（km）
   */
  data class Car(
      val id: Long,
      val name: String,
      val mileage: Int
  )
  ```

#### Composable関数のプレビュー
- すべてのComposable関数には対応する`@Preview`関数を記載する
- プレビュー関数名は`{関数名}Preview`とする
- 例:
  ```kotlin
  @Composable
  fun CarCard(car: Car) { ... }

  @Preview(showBackground = true)
  @Composable
  private fun CarCardPreview() {
      CarCard(car = Car(id = 1, name = "Sample Car", mileage = 10000))
  }
  ```

### テスト方針（MVP段階）
- UI: 手動確認
- Domain: UseCase単体テスト（任意）
- Data: DAOテスト（必要に応じて）

### 環境要件
- Android Studio Hedgehog 以降
- minSdk = 26
- targetSdk = 最新
- Compose Compiler Version = 1.5.1