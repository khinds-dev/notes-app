# Notes App — Project Context

## Project Overview
A simple Android notes app built with Jetpack Compose and Material 3. The goal is a minimal, clean
note-taking experience: flat list of notes (no folders), with create/edit/delete and search.

## Package & Build
- **Application ID:** `com.keith.notesapp`
- **App name:** Notes
- **minSdk:** 26 (Android 8.0), **compileSdk / targetSdk:** 35
- **Language:** Kotlin 2.1, JVM target 17
- **Build tool:** Gradle with Kotlin DSL + version catalog (`gradle/libs.versions.toml`)

### Build commands
```bash
./gradlew assembleDebug        # build debug APK
./gradlew installDebug         # build and install on connected device/emulator
./gradlew test                 # unit tests
./gradlew lint                 # lint check
```

## Architecture
Clean Architecture + MVVM, three layers:

```
data/       → Room entities, DAOs, database, repository implementations
domain/     → Plain model (Note), repository interface, use cases
presentation/ → ViewModels, Compose screens, navigation, theme
```

### Key conventions
- **One `NoteUseCases` data class** bundles all six use cases; injected via Hilt into ViewModels.
- **Repository interface** lives in `domain/`; implementation in `data/`.
- **Mapping** between `NoteEntity` ↔ `Note` is done entirely inside `NoteRepositoryImpl` —
  no mapper classes.
- **ViewModels** are annotated `@HiltViewModel` and use `SavedStateHandle` where route args are needed.
- **Screens** receive only lambdas and call `hiltViewModel()` internally — no ViewModel passed from nav.
- **Navigation** is in `NotesNavHost.kt`; routes are private string constants in that file.

## Directory Structure

```
app/src/main/java/com/keith/notesapp/
├── NotesApp.kt                          @HiltAndroidApp Application class
├── MainActivity.kt                      Single activity, sets theme + nav host
├── data/
│   ├── di/AppModule.kt                  Hilt module: DB → Repo → UseCases
│   ├── local/
│   │   ├── NotesDatabase.kt             Room database (version 1)
│   │   ├── entity/NoteEntity.kt         id, title, content, timestamp
│   │   └── dao/NoteDao.kt               getAllNotes, searchNotes, getNoteById, insert, update, delete
│   └── repository/NoteRepositoryImpl.kt maps entity ↔ domain model
├── domain/
│   ├── model/Note.kt                    Plain data class (id, title, content, timestamp)
│   ├── repository/NoteRepository.kt     Interface
│   └── usecase/NoteUseCases.kt          6 use cases + NoteUseCases bundle
└── presentation/
    ├── theme/                           Material 3 theme, dynamic colour on API 31+
    ├── navigation/NotesNavHost.kt       note_list ↔ note_edit/{noteId}
    ├── viewmodel/
    │   ├── NoteListViewModel.kt         search debounce, delete, undo-insert
    │   └── NoteEditViewModel.kt         load/save by noteId (-1 = new note)
    └── screen/
        ├── NoteListScreen.kt            staggered grid, search bar, delete + undo snackbar
        └── NoteEditScreen.kt            transparent title + body fields, auto-save on back
```

## Data Model
`Note(id: Long, title: String, content: String, timestamp: Long)`

- `id = 0` → Room auto-generates the ID on insert
- `id = -1` in the nav route means "new note" (ViewModel detects this)
- Notes are ordered by `timestamp DESC`
- Search matches both `title` and `content` (SQL `LIKE`)

## Dependencies (from `gradle/libs.versions.toml`)
| Library | Purpose |
|---|---|
| Compose BOM | All Compose UI libraries |
| Material 3 | UI components and theming |
| Navigation Compose | Screen navigation |
| Hilt 2.54 | Dependency injection |
| Room 2.6 | Local SQLite database |
| Lifecycle / ViewModel | StateFlow, collectAsStateWithLifecycle |

## What This App Does NOT Have
- Folders or categories (flat list only)
- Markdown rendering
- Biometrics / lock screen
- Widgets
- Export / import
- Cloud sync
- Any permissions (no manifest `<uses-permission>` entries)

Keep future features consistent with this intentional simplicity unless explicitly asked to add them.
