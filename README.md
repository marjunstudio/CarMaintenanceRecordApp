# 📘 Car Maintenance App（仮） - README (MVP)
車のメンテナンス記録をシンプルに管理できる Android アプリ。
オイル交換、タイヤ交換、車検などの履歴を記録し、次回メンテナンスのタイミングも直感的に把握できます。
MVP では、最低限のメンテナンス管理を快適に行える機能のみ実装しています。
---
# 🚀 MVP で提供する機能
### ▼ **車両管理**
* 車を 1 台登録
* 車名・走行距離・写真（任意）の保存
* 走行距離の手動更新
### ▼ **メンテナンス履歴管理**
* メンテナンス記録の登録
* 種類（オイル交換、タイヤ、車検など）
* 日付
* 走行距離
* 金額（任意）
* メモ（任意）
* メンテナンス履歴の一覧表示（最新順）
### ▼ **次回メンテナンスの表示**
* 「次のメンテまでの距離／日数」を表示
* ユーザー設定の目安値（例：オイル交換 5000km）に基づいて計算
### ▼ **ホーム画面**
* 車情報の概要
* 次回メンテの残り距離
* 最近のメンテ履歴3件の表示
---
# 🏗 使用技術（Tech Stack）
### ▼ **言語**
* Kotlin（100%）
### ▼ **UI**
* Jetpack Compose
* Material 3
* Navigation Compose
### ▼ **状態管理**
* ViewModel (AndroidX)
* StateFlow / MutableStateFlow
* 画面単位の UI State を採用
### ▼ **データ**
* Room Database
* Dao / Entity / Repositoryパターン
* DataStore（必要に応じて設定値保存で使用予定）
### ▼ **DI**
* Hilt（推奨）
### ▼ **アーキテクチャ**
* MVVM
* Presentation / Domain / Data の緩めのクリーンアーキ
* ViewModel → UseCase → Repository → Room の依存構造
### ▼ **開発方針**
* あくまで Android アプリとして最適な構成
* ただし、将来的に KMP 移行しやすいよう Domain/Data を UI から分離
---
# 📁 プロジェクト構成（MVP時点）
```
com.example.carmaintenance
├─ ui # Compose UI / ViewModel / UiState
│ ├─ home
│ ├─ car
│ └─ maintenance
│
├─ domain # ビジネスロジック
│ ├─ model
│ └─ usecase
│
├─ data # Repository / Room / Mapper
│ ├─ repository
│ ├─ local
│ │ ├─ dao
│ │ ├─ entity
│ │ └─ AppDatabase.kt
│ └─ mapper
│
└─ di # Hilt Module
```
---
# 🧩 MVP 以降に予定している機能（未来のロードマップ）
※現段階では実装しないが、将来的には実装したい機能リスト
### ▼ バージョン 1.1
* メンテ項目のテンプレ変更
* フィルター（オイル / タイヤ / 車検）
### ▼ バージョン 1.2
* 通知リマインダー（期間/走行距離ベース）
* 写真添付（領収書・メンテの記録用）
### ▼ バージョン 1.3
* 車を複数台登録
* CSVエクスポート
### ▼ KMP 対応（別フェーズ）
* Domain/Data を Kotlin Multiplatform に移動
* iOS版（SwiftUI or Compose Multiplatform）
---
# 🧪 テスト方針（MVP）
* UI：手動確認
* Domain：UseCase単体テスト（任意）
* Data：DAOテスト（必要なら）
---
# 📦 ビルド & 実行
```
Android Studio Hedgehog 以降
minSdk = 26
targetSdk = 最新
composeCompilerVersion = 最新
```
Run → App
---
# 📜 ライセンス
後ほど決定（MIT / Apache-2.0 を想定）
---