# Repository Guidelines

## Project Structure & Module Organization
This is a single-module Android app rooted in `app/`. Kotlin sources live in `app/src/main/java/to/msn/wings/carmaintenancerecord`, split into `ui` (Compose screens + `ViewModel` per feature such as `ui/home`, `ui/car`, `ui/maintenance`), `domain` (models and use cases), `data` (Room entities, DAO, repository, mapper), and `di` for Hilt modules. Resources stay under `app/src/main/res`, while shared assets (fonts, icons) go into the matching `res` subfolders. Unit tests sit in `app/src/test`, instrumentation/UI tests in `app/src/androidTest`, and Gradle/ksp configuration is defined in the root `build.gradle.kts`, `settings.gradle.kts`, and `gradle/libs.versions.toml`.

## Build, Test, and Development Commands
- `./gradlew assembleDebug` — builds the debug APK that Android Studio installs when you press Run.
- `./gradlew lintDebug` — runs the Android lint checks; fix findings before opening a PR.
- `./gradlew testDebugUnitTest` — executes JVM unit tests in `app/src/test` (domain/use case coverage lives here).
- `./gradlew connectedDebugAndroidTest` — runs instrumentation and Compose UI tests on an attached device or emulator.

## Coding Style & Naming Conventions
Use Kotlin style with 4-space indentation, trailing commas for multiline parameters, and expression bodies when a function fits in one line. Compose `@Composable` functions, `ViewModel` classes, and Room entities all use UpperCamelCase (`CarDetailScreen`, `MaintenanceViewModel`, `MaintenanceEntity`). Private helpers and Flow properties use lowerCamelCase (`nextMaintenanceUiState`). Keep feature packages flat (`ui.home`, `domain.usecase`, `data.repository`). Run `./gradlew lintDebug` before pushing to ensure formatting consistency.

## Testing Guidelines
Unit tests should mirror the feature package they cover, naming files as `<Feature>UseCaseTest` or `<DaoName>Test`. Compose/UI tests reside in `androidTest` with function names formatted as `shouldDisplayNextMaintenance_whenReminderExists`. Target at least one test per use case and DAO, and add regression tests whenever you patch a bug. Prefer fake repositories over hitting Room in JVM tests; reserve instrumented tests for flows that require Hilt, Navigation, or DataStore.

## Commit & Pull Request Guidelines
Follow the existing concise, Japanese commit style (`車両登録・編集でタイトルが切り替わるように修正`) and keep messages imperative. Each PR should include: summary of the change, linked issue or task ID, screenshots or screen recordings for UI-facing updates, and a checklist of manual test steps (device/emulator, API level). Ensure CI commands above pass locally, note any migrations, and tag reviewers responsible for the touched feature area.
