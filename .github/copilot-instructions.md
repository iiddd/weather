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