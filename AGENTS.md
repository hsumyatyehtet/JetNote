# JetNote — Agent context

Android notes app by **Hmyh**. Kotlin, Jetpack Compose (Material 3), single-module Gradle project.

## Tech stack

| Area | Choice |
|------|--------|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material 3 |
| DI | Dagger Hilt (wired at app/activity; modules mostly empty) |
| Persistence (planned) | Room 2.7 (dependencies present; no DB/DAO/entities yet) |
| Async | Kotlin Coroutines (on classpath; ViewModel uses in-memory list today) |
| Build | AGP 8.13, `compileSdk`/`targetSdk` 36, `minSdk` 24, JVM 11 |
| Package | `com.hmyh.jetnote` |

## Repository layout

```
JetNote/
├── app/                          # sole Android module
│   ├── build.gradle.kts          # app deps, Compose, Hilt, Room, KSP
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/hmyh/jetnote/
│       │   │   ├── MainActivity.kt       # @AndroidEntryPoint, hosts Compose
│       │   │   ├── NoteApplication.kt    # @HiltAndroidApp
│       │   │   ├── components/             # reusable Compose widgets
│       │   │   ├── data/                   # DTOs + local/fake data sources
│       │   │   ├── di/                     # Hilt modules
│       │   │   ├── model/                  # domain/data models
│       │   │   ├── screen/                 # screens, ViewModels, screen composables
│       │   │   └── ui/theme/               # Color, Theme, Typography
│       │   └── res/                        # strings, themes, drawables
│       ├── test/                           # unit tests (JUnit 4)
│       └── androidTest/                    # instrumented + Compose UI tests
├── gradle/libs.versions.toml     # version catalog (prefer aliases here)
├── settings.gradle.kts         # root name "JetNote", includes :app
└── build.gradle.kts              # root plugin aliases (apply false)
```

## Architecture (current)

Layered by package, not strict Clean Architecture yet:

```
MainActivity (Compose host, Scaffold)
    └── NotesApp composable
            └── NoteViewModel (in-memory mutableStateListOf)
                    └── NoteDataSource.loadNotes()  // seed data
            └── NoteScreen (UI + local state for form)
                    └── components: NoteInputText, NoteButton
                    └── model: Note
```

**Data flow today**

1. `NoteViewModel` loads initial notes from `NoteDataSource` in `init`.
2. `MainActivity` obtains `NoteViewModel` via `viewModels()` and passes callbacks to `NoteScreen`.
3. Add/remove mutate the ViewModel list; UI reads `getAllNotes()` (list reference from `mutableStateListOf` drives recomposition when used from Compose).

**Planned / partial**

- **Hilt**: `NoteApplication`, `MainActivity` annotated; `AppModule` is empty; `NoteViewModel` is **not** `@HiltViewModel` yet.
- **Room**: libraries + KSP in `app/build.gradle.kts`; no `@Entity`, `@Dao`, or `RoomDatabase` classes.

## Package conventions

| Package | Put here |
|---------|----------|
| `com.hmyh.jetnote` | Application entry, `MainActivity`, top-level app composables |
| `...model` | `data class` domain models (e.g. `Note`) |
| `...data` | DTOs (`UserData`) and data sources / repositories (e.g. `NoteDataSource` in `NoteData.kt`) |
| `...screen` | Feature screens, feature ViewModels, screen-specific composables (`NoteRow`) |
| `...components` | Shared Compose UI used across screens |
| `...di` | `@Module` / `@InstallIn` Hilt bindings |
| `...ui.theme` | `JetNoteTheme`, colors, typography |

When adding features, prefer **one screen package per feature** or extend `screen/` with clear names (`FooScreen.kt`, `FooViewModel.kt`).

## Key types

- **`Note`** (`model`): `id: UUID`, `title`, `description`, `entryDate: LocalDateTime` (defaults). Requires API 26+ (`@RequiresApi(Build.VERSION_CODES.O)`).
- **`UserData`** (`data`): nullable `name`, `age`, `nrc` — all default `null`.
- **`NoteDataSource`** (`data/NoteData.kt`): fake in-memory list of sample notes.

## UI patterns

- Theme: `JetNoteTheme` in `ui/theme/Theme.kt`; edge-to-edge enabled in `MainActivity`.
- Screens use Material 3 (`TopAppBar`, `LazyColumn`, `Surface`).
- Input validation on note form: letters and whitespace only for title/description.
- Tap note row → removes note (via `onRemoveNote`).
- Previews: `@Preview` on `NoteScreen` with `NoteDataSource().loadNotes()`.

## Coding standards for this repo

- Match existing style: 4-space indent, Kotlin `data class` for models, Compose in `screen` or `components`.
- Use `stringResource(R.string.*)` for user-visible app name where already done.
- New API 26+ time APIs: follow `Note` and annotate with `@RequiresApi(O)` or guard minSdk if lowering requirements.
- Prefer **version catalog** (`libs.versions.toml`) for new dependencies.
- Do not add unrelated modules; project is single `:app` module.
- Keep changes minimal; avoid over-abstracting until Room/Hilt are fully adopted.

## Where to implement common tasks

| Task | Location |
|------|----------|
| New screen | `screen/NewFeatureScreen.kt` + ViewModel in same package |
| Shared widget | `components/` |
| New model | `model/` |
| DB entity / DAO | new `data/` or `data/local/` (Room not started — establish pattern once) |
| Hilt binding | `di/AppModule.kt` or new module under `di/` |
| Strings / theme | `res/values/`, `ui/theme/` |
| Entry / navigation | `MainActivity.kt` (no Navigation-Compose dependency yet) |

## Tests

- Unit: `app/src/test/java/com/hmyh/jetnote/`
- Android/Compose UI: `app/src/androidTest/java/com/hmyh/jetnote/`

## Build commands

```bash
./gradlew assembleDebug
./gradlew test
./gradlew connectedAndroidTest   # device/emulator required
```
