# Copilot instructions for this repository

## Language
•	Generate Kotlin code only
•	Do not output code in other languages unless explicitly requested

## Naming conventions (strict)
•	Do NOT abbreviate words in names
•	Use full, explicit names for:
•	functions
•	methods
•	variables
•	parameters
•	classes
•	objects
•	constants

### Examples

#### Correct:
•	navigationBackStack
•	destinationArguments
•	selectedDestination
•	replaceCurrentDestination
•	navigationEntryProvider

#### Incorrect:
•	nav
•	args
•	dest
•	curr
•	cfg
•	vm (use ViewModel)
•	repo (use Repository)

If unsure, always prefer a full descriptive name.

## Kotlin style rules

### Named arguments
•	Always use Kotlin named arguments when calling functions or constructors
•	Exceptions:
•	the function has exactly one parameter
•	very common standard library calls where names reduce readability

#### Correct:
NavEntry(
destination = destination
) {
DetailedWeatherScreen(
initialLatitude = destination.latitude,
initialLongitude = destination.longitude
)
}

#### Incorrect:
NavEntry(destination) {
DetailedWeatherScreen(destination.latitude, destination.longitude)
}

### Lambdas
•	Always use explicit parameter names in lambdas
•	Do NOT use _ for ignored parameters unless absolutely unavoidable

#### Correct:
onBack = {
navigationBackStack.pop()
}

#### Incorrect:
onBack = { _ -> backStack.pop() }

### Jetpack Compose rules
•	Prefer explicit state names
•	selectedTabDestination instead of selectedTab
•	Avoid shortened composable names
•	Prefer immutable state where possible
•	Do not hide important logic inside remember without clear intent

## Architecture assumptions
•	MVVM
•	Unidirectional data flow
•	Explicit navigation state objects
•	Navigation destinations are value objects
•	Navigation stack is an explicit mutable list

## Output expectations
•	Prefer correctness and clarity over brevity
•	Avoid speculative or placeholder APIs
•	Do not invent abstractions unless explicitly requested
•	When unsure, ask instead of guessing

---

# App business rules (must be enforced)

## Screens and Tabs (global)
- The application has 3 screens:
    - Home (DetailedWeatherScreen)
    - Search (Map + Search input)
    - Settings
- Bottom tabs exist for each screen.
- When a screen is visible, its tab must be marked as selected.
- Tab selection must be derived from the current destination, not duplicated local state.

## Unified request mechanism
- All API requests must use a single shared mechanism (common networking client + consistent request wrapper).
- There must be a single unified error handling approach.
- UI must not implement ad-hoc try/catch per screen for network errors.

## Global UI states
- While a request is running and takes noticeable time, show a generic Loading screen.
- On any failure (including timeout or location unavailable), show a generic Error screen.
- Error screen must support retry action via a provided lambda.
- Loading and Error must be based on explicit UI state models, not on scattered boolean flags.

Recommended UI state approach:
- Use a sealed interface/class such as:
    - Loading
    - Error
    - Content

## Startup behavior
- When the application starts:
    - attempt to obtain current device location
    - attempt reverse geocoding (city name) when applicable
    - request weather for that location
- The initial destination shown is Home (DetailedWeatherScreen) presenting weather for current location.
- If location is unavailable or any request fails, show the generic Error screen.

## DetailedWeatherScreen behavior
- DetailedWeatherScreen must have a Refresh action.
- Refresh triggers a new weather request for the currently displayed coordinates.
- Refresh must reuse the same shared request mechanism and error handling.
- Refresh must show Loading when in progress and Error on failure.

## SearchScreen behavior
- SearchScreen contains:
    - Google Maps
    - a search text input
- When the user submits (Search button or IME action):
    - perform forward geocoding for the most likely matching city
    - move the map camera to the resulting coordinates
    - show a marker popup with the full resolved place name and a Select action
- When the user presses Select:
    - navigate to Home (DetailedWeatherScreen) using the selected coordinates
    - Home must load weather for those coordinates and show Content/Loading/Error accordingly

## Navigation and data passing
- Navigation to DetailedWeatherScreen from Search must pass coordinates as destination arguments.
- Do not store navigation arguments in global mutable singletons.
- Weather loading must be triggered from Route via a provided lambda.

## Location and permissions
- Location acquisition and permissions must remain outside UI screens:
    - use dedicated permission and location helper files
    - expose usage via remember* controllers/providers
- If location is denied/unavailable, show generic Error screen rather than partial UI.

## UI composition conventions (strict)

### Layered Compose structure (mandatory)

All screens **must** follow this structure:
NavigationHost -> *Route -> *Screen -> *ScreenContent

### Responsibilities per layer

#### Route (`*Route`)
- Owns side effects and orchestration
- Responsibilities:
    - Dependency injection (ViewModel, controllers)
    - `collectAsStateWithLifecycle`
    - `LaunchedEffect`
    - Permission and location triggers
    - Deciding **when** data should be loaded
    - Calling `ViewModel.onEvent(...)`

- Route **may**:
    - Hold transient UI coordination state (for example: latestLoadedKey)
    - Gate repeated requests

- Route **must not**:
    - Render UI directly
    - Contain layout logic

---

#### Screen (`*Screen`)
- Pure UI state rendering layer
- Responsibilities:
    - Render based on explicit UI state models (`Loading`, `Error`, `Content`)
    - Forward user intents via lambdas

- Screen **must not**:
    - Perform side effects
    - Request permissions
    - Access location APIs
    - Trigger network calls directly
    - Use `LaunchedEffect`

---

#### ScreenContent (`*ScreenContent`)
- Stateless layout-only composable
- Responsibilities:
    - Layout, visuals, previews

- ScreenContent **must**:
    - Be fully previewable
    - Accept plain data models

- ScreenContent **must not**:
    - Use ViewModel
    - Use `rememberSaveable`
    - Use `LaunchedEffect`
    - Perform navigation or side effects

---

### Permissions and location

- Permission handling and location acquisition **must always live in Route**
- UI must react only to explicit UI state (`Error` if unavailable)
- Partial or degraded UI is not allowed when permissions are missing

---

### Data loading invariant

- Network and location requests:
    - Must be triggered exactly once per logical navigation entry
    - Must be guarded against recomposition-triggered repeats
    - Must never be triggered from `Screen` or `ScreenContent`

---

### Preview data

- Preview-only data providers:
    - Must live in `presentation.previewfixtures`
    - Must not leak into domain or data layers
    - Must be safe to use in multiple previews

