# Notes App

A minimal Android note-taking app built with Jetpack Compose and Material 3. Flat list of notes with create, edit, delete, and search — no folders, no complexity.

## Features

- Create, edit, and delete notes
- Search notes by title or content
- Staggered grid layout on the note list
- Undo delete via snackbar
- Auto-save on back navigation
- Dynamic colour theming on Android 12+

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.8.5 |
| DI | Hilt 2.54 |
| Database | Room 2.6.1 |
| Architecture | Clean Architecture + MVVM |
| Language | Kotlin 2.1 |
| Min SDK | 26 (Android 8.0) |

## Architecture

Three-layer Clean Architecture:

```
data/         → Room entities, DAOs, database, repository implementations
domain/       → Plain model, repository interface, use cases
presentation/ → ViewModels, Compose screens, navigation, theme
```

## Project Structure

```
app/src/main/java/com/keith/notesapp/
├── NotesApp.kt                          @HiltAndroidApp Application class
├── MainActivity.kt                      Single activity
├── data/
│   ├── di/AppModule.kt                  Hilt module
│   ├── local/
│   │   ├── NotesDatabase.kt             Room database
│   │   ├── entity/NoteEntity.kt         id, title, content, timestamp
│   │   └── dao/NoteDao.kt               CRUD + search queries
│   └── repository/NoteRepositoryImpl.kt entity ↔ domain mapping
├── domain/
│   ├── model/Note.kt                    Plain data class
│   ├── repository/NoteRepository.kt     Interface
│   └── usecase/NoteUseCases.kt          6 use cases bundled
└── presentation/
    ├── theme/                           Material 3 theme
    ├── navigation/NotesNavHost.kt       note_list ↔ note_edit/{noteId}
    ├── viewmodel/
    │   ├── NoteListViewModel.kt         search debounce, delete, undo
    │   └── NoteEditViewModel.kt         load/save by noteId (-1 = new)
    └── screen/
        ├── NoteListScreen.kt            staggered grid + search bar
        └── NoteEditScreen.kt            title + body fields, auto-save
```

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run lint
./gradlew lint
```

## Data Model

```kotlin
Note(id: Long, title: String, content: String, timestamp: Long)
```

- `id = 0` → Room auto-generates the ID on insert
- `id = -1` in the nav route → new note
- Notes are ordered by `timestamp DESC`
- Search matches both `title` and `content` (SQL `LIKE`)
