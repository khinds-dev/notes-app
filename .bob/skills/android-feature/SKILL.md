---
name: android-feature
description: Use when the user wants to add a new feature, screen, or capability to the notes app — walks through the full Clean Architecture chain from data layer to UI in the correct order.
---

# Add a New Feature to the Notes App

Follow these steps in order whenever a new feature, screen, or data capability is requested.

## Step 1 — Clarify scope before touching any files

Use `ask_followup_question` to confirm:
- What data (if any) needs to be persisted? Does it belong on `NoteEntity` or need a new entity?
- Is a new screen needed, or is this a change to an existing screen?
- Does the feature require a new use case, or can an existing one be reused?

Do not start writing until the answers are clear.

## Step 2 — Data layer (if persistence is involved)

Work in this order:

1. **Entity** — add fields to `NoteEntity.kt` or create a new `@Entity` in `data/local/entity/`.
   - Increment the Room `version` in `NotesDatabase.kt` and add a `Migration` object if modifying
     an existing table. Never use `fallbackToDestructiveMigration`.
2. **DAO** — add query methods to `NoteDao.kt` (or a new DAO file). Return `Flow<T>` for
   observable queries; use `suspend` for one-shot writes.
3. **Database** — register any new entity in the `@Database` annotation and expose the new DAO.
4. **Repository interface** — add the new method signature to `domain/repository/NoteRepository.kt`.
5. **Repository implementation** — implement the method in `NoteRepositoryImpl.kt`.
   All entity ↔ domain mapping stays inside this file via private extension functions.

## Step 3 — Domain layer

1. **Model** — add fields to `Note.kt` or create a new domain model in `domain/model/` if the
   feature is conceptually separate.
2. **Use case** — create a new use case class in `domain/usecase/NoteUseCases.kt` following the
   existing pattern (a class with an `operator fun invoke`).
3. **NoteUseCases bundle** — add the new use case as a property of the `NoteUseCases` data class.
4. **AppModule** — add the new use case to `provideNoteUseCases(...)` in `data/di/AppModule.kt`.

## Step 4 — Presentation layer

### New screen
1. Create a `@HiltViewModel` in `presentation/viewmodel/`. Inject `NoteUseCases` (not individual
   use cases directly). Use `SavedStateHandle` for any nav route arguments.
2. Expose state as `StateFlow`; use `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ...)`.
3. Create a `@Composable` screen function in `presentation/screen/`. Call `hiltViewModel()`
   internally — do not pass the ViewModel from the nav graph.
4. Register the route in `NotesNavHost.kt`:
   - Add a private `const val` route string.
   - Add a `composable(...)` block.
   - Add a lambda parameter to any calling screen that triggers navigation to the new route.

### Modifying an existing screen
- Extend the relevant ViewModel state and add event handler functions.
- Update the Composable screen accordingly.
- Do not add business logic inside Composable functions.

## Step 5 — Verify the wiring

Check in order:
- `AppModule` provides the new use case and it compiles (no missing Hilt bindings).
- `NoteUseCases` data class includes the new use case.
- Navigation route string is consistent between `NavHost` definition and call sites.
- No direct DAO or database access from outside the `data/` package.
- No Android framework imports (`Context`, `Activity`, etc.) inside `domain/`.

## Conventions to preserve

- **No folders** — the app is intentionally flat. Do not add folder/category concepts.
- **No Markdown rendering** — plain text only unless explicitly asked.
- **Mapping in repository only** — `NoteEntity` ↔ `Note` conversions belong in `NoteRepositoryImpl`.
- **Package:** all new files under `com.keith.notesapp.*` matching their layer.
- **Minimal permissions** — do not add `<uses-permission>` entries unless the feature genuinely
  requires a system permission.
