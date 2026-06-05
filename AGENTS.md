# JetNote — Agent context

Android notes app by **Hmyh**. Kotlin, Jetpack Compose (Material 3), single-module Gradle project.

## Tech stack

| Area | Choice |
|------|--------|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material 3 |
| DI | Dagger Hilt (`NoteApplication`, `MainActivity`, `AppModule`) |
| Persistence | Room 2.7 + KSP (`NoteDatabase`, `NoteDatabaseDao`, `Converters`) |
| Async | Kotlin Coroutines + `Flow` (DAO ready; ViewModel still in-memory) |
| Build | AGP 8.13, `compileSdk`/`targetSdk` 36, `minSdk` 24, JVM 11 |
| Package | `com.hmyh.jetnote` |

## Repository layout

```
JetNote/
├── app/                          # sole Android module
│   ├── build.gradle.kts          # Compose, Hilt, Room, KSP
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/hmyh/jetnote/
│       │   │   ├── MainActivity.kt       # @AndroidEntryPoint, hosts Compose + navigation state
│       │   │   ├── NoteApplication.kt    # @HiltAndroidApp
│       │   │   ├── components/           # reusable Compose widgets (NoteInputText, NoteButton)
│       │   │   ├── data/
│       │   │   │   ├── Converters.kt     # Room TypeConverters (Date, UUID)
│       │   │   │   ├── NoteData.kt       # NoteDataSource — fake seed data
│       │   │   │   ├── NoteDatabase.kt   # @Database, @TypeConverters
│       │   │   │   └── NoteDatabaseDao.kt
│       │   │   ├── di/
│       │   │   │   └── AppModule.kt      # provides NoteDatabase + NoteDatabaseDao
│       │   │   ├── model/
│       │   │   │   └── Note.kt           # @Entity Room entity
│       │   │   ├── screen/
│       │   │   │   ├── NoteViewModel.kt
│       │   │   │   ├── NoteScreen.kt     # list + add form
│       │   │   │   └── NoteDetailScreen.kt
│       │   │   └── ui/theme/             # Color, Theme, Typography
│       │   └── res/
│       ├── test/
│       └── androidTest/
├── gradle/libs.versions.toml
├── settings.gradle.kts
└── build.gradle.kts
```

## Architecture (current)

Layered by package, not strict Clean Architecture yet:

```
NoteApplication (@HiltAndroidApp)
    └── AppModule
            ├── NoteDatabase ("notes_db")
            │       └── NoteDatabaseDao  (wired, not used by ViewModel yet)
            └── Converters on @Database

MainActivity (@AndroidEntryPoint)
    └── NotesApp
            ├── NoteViewModel (in-memory mutableStateListOf)
            │       └── NoteDataSource.loadNotes()  // seed data
            ├── NoteScreen (list, add, delete)
            └── NoteDetailScreen (tap row → detail; back clears selection)
```

**Data flow today**

1. `NoteViewModel` seeds notes from `NoteDataSource` in `init`; holds `mutableStateListOf<Note>`.
2. `MainActivity` obtains `NoteViewModel` via `viewModels()` and passes callbacks to `NoteScreen`.
3. Add/remove mutate the in-memory list; UI reads `getAllNotes()`.
4. Tap a note row sets `selectedNote` in `NotesApp` and shows `NoteDetailScreen`.
5. Room layer exists and is Hilt-provided, but **ViewModel does not inject or call the DAO yet**.

**Wired / partial**

| Area | Status |
|------|--------|
| Hilt app entry | `NoteApplication`, `MainActivity` annotated |
| Hilt modules | `AppModule` provides `NoteDatabase` + `NoteDatabaseDao` |
| Room entity | `Note` in `model/` with `@Entity`, `@PrimaryKey`, `@ColumnInfo` |
| Room converters | `Converters.kt` — `Date` ↔ `Long`, `UUID` ↔ `String`; registered on `NoteDatabase` |
| ViewModel + Room | **Not connected** — still uses `NoteDataSource`, not `@HiltViewModel` |
| Navigation | State in `NotesApp` (`selectedNote`); no Navigation-Compose dependency |

## Package conventions

| Package | Put here |
|---------|----------|
| `com.hmyh.jetnote` | Application entry, `MainActivity`, top-level composables (`NotesApp`) |
| `...model` | Room `@Entity` models (e.g. `Note`) |
| `...data` | DTOs, fake sources, Room DB/DAO, `TypeConverter`s |
| `...screen` | Feature screens, ViewModels, screen-specific composables (`NoteRow`) |
| `...components` | Shared Compose UI used across screens |
| `...di` | `@Module` / `@InstallIn` Hilt bindings |
| `...ui.theme` | `JetNoteTheme`, colors, typography |

When adding features, prefer **one screen per feature** under `screen/` (`FooScreen.kt`, `FooViewModel.kt`).

## Key types

- **`Note`** (`model/Note.kt`): Room `@Entity(tableName = "notes_tbl")`. Fields: `id: UUID`, `title`, `description`, `entryDate: Date` (default `Date.from(Instant.now())`). Requires API 26+ (`@RequiresApi(Build.VERSION_CODES.O)`). Non-primitive fields need converters (see `Converters.kt`).
- **`Converters`** (`data/Converters.kt`): `Date` stored as epoch `Long`; `UUID` stored as `String`.
- **`NoteDatabase`** (`data/NoteDatabase.kt`): version 1, `exportSchema = false`, `@TypeConverters(Converters::class)`.
- **`NoteDatabaseDao`** (`data/NoteDatabaseDao.kt`): `getNote(): Flow<List<Note>>`, insert/update/delete. **Fix pending:** `getNoteById(id: Int)` should use `UUID` to match `Note.id`.
- **`NoteDataSource`** (`data/NoteData.kt`): fake in-memory list of sample notes (used by ViewModel seed + `@Preview`).
- **`UserData`** (`data`): nullable `name`, `age`, `nrc` — all default `null` (if present).

## Room conventions

- Register `@TypeConverters` on `NoteDatabase` (or per-entity) for any non-primitive column type.
- Supported without converters: `Int`, `Long`, `String`, `Boolean`, `Float`, `Double`, `ByteArray`.
- `java.util.Date`, `java.util.UUID`, and `java.time.*` types **require** `@TypeConverter` pairs.
- Prefer adding converters in `data/Converters.kt` rather than changing entity field types, unless simplifying to `Long`/`String` directly.

## UI patterns

- Theme: `JetNoteTheme` in `ui/theme/Theme.kt`; edge-to-edge enabled in `MainActivity`.
- Screens use Material 3 (`TopAppBar`, `LazyColumn`, `Surface`).
- Input validation on note form: letters and whitespace only for title/description.
- Delete icon on row removes note; tap row body opens `NoteDetailScreen`.
- Previews: `@Preview` with `NoteDataSource().loadNotes()`.

## Coding standards for this repo

- Match existing style: 4-space indent, Kotlin `data class` for models, Compose in `screen` or `components`.
- Use `stringResource(R.string.*)` for user-visible app name where already done.
- API 26+ time APIs: annotate with `@RequiresApi(O)` on types/composables that use `Instant`, `Date`, etc.
- Prefer **version catalog** (`libs.versions.toml`) for new dependencies.
- Single `:app` module only; keep diffs minimal.
- When wiring Room into ViewModel: prefer `@HiltViewModel` + inject `NoteDatabaseDao`, collect `Flow<List<Note>>` in `viewModelScope`.

## Where to implement common tasks

| Task | Location |
|------|----------|
| New screen | `screen/NewFeatureScreen.kt` + ViewModel in same package |
| Shared widget | `components/` |
| Room entity | `model/` (current pattern: entity lives with domain model) |
| DAO / database | `data/NoteDatabaseDao.kt`, `data/NoteDatabase.kt` |
| Type converters | `data/Converters.kt`; register on `NoteDatabase` |
| Hilt binding | `di/AppModule.kt` |
| Wire ViewModel to DB | `screen/NoteViewModel.kt` — `@HiltViewModel`, inject DAO, replace `NoteDataSource` seed |
| Strings / theme | `res/values/`, `ui/theme/` |
| Entry / navigation | `MainActivity.kt` → `NotesApp` (state-based; add Navigation-Compose only if requested) |

## Tests

- Unit: `app/src/test/java/com/hmyh/jetnote/`
- Android/Compose UI: `app/src/androidTest/java/com/hmyh/jetnote/`

## Build commands

```bash
./gradlew assembleDebug
./gradlew test
./gradlew connectedAndroidTest   # device/emulator required
```
