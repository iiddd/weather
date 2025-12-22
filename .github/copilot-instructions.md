# Copilot Instructions

This is a modern Android application.

## Platform
- Android only
- minSdk = 31
- compileSdk = 36
- Java/Kotlin (Project only uses Kotlin) target = 21

## UI
- Jetpack Compose only
- No XML layouts
- Material 3

## Navigation
- Use Jetpack Navigation 3 (androidx.navigation3)
- Navigation stack is a mutable list of Destination objects
- Destinations are sealed objects that hold state and navigation arguments
- No XML navigation graphs
- No Fragments
- No SafeArgs
- No Voyager

## Architecture
- MVVM + Clean architecture pattern
- SOLID
- Feature-based modularisation
- Each feature module owns its ViewModels and repository implementations
- core modules contain only shared abstractions and utilities

## State & lifecycle
- Navigation state must survive configuration changes
- Do not pass Android Context through navigation
- Avoid global singletons

## What NOT to suggest
- Fragment-based navigation
- XML NavHost or NavHostFragment
- Legacy Navigation Component

## Naming conventions (important)

- Do NOT abbreviate words in names.
- Use full, explicit names for:
    - functions
    - methods
    - variables
    - parameters
    - classes
    - objects
    - constants

### Examples

Correct:
- navigationBackStack
- destinationArguments
- selectedDestination
- replaceCurrentDestination
- navigationEntryProvider

Incorrect:
- nav
- args
- dest
- curr
- cfg
- vm (use ViewModel)
- repo (use Repository)

### Allowed abbreviations
- Widely accepted technical abbreviations only:
    - UI, UX
    - API
    - URL
    - ID
    - DTO
    - JSON
    - HTTP
    - JVM

If unsure, prefer a full descriptive name.