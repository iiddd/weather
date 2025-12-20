# ☀️ Weather App

A modern multi-module **Android Weather App** built entirely with **Kotlin** and **Jetpack Compose**, following **Clean Architecture**, **MVVM**, and **SOLID** principles.  
The app uses **OpenWeatherMap One Call API 3.0** as its primary data source and integrates **Google Maps API** for city search and location detection.

---

## 🚀 Project Goals

- Practice **Jetpack Compose** UI
- Learn and apply **Koin** for dependency injection
- Implement **Clean Architecture + MVVM**  
- Implement **modularization** in Android projects
- Use **Kotlin Coroutines** and **Flow** for asynchronous programming
- Use **Retrofit** with **Kotlinx Serialization** for networking
- Implement **location services** with FusedLocationProvider
- Implement Navigation with **Navigation3**
- Integrate **OpenWeather** and **Google Maps** APIs

---

## 🧩 Architecture Overview

The project follows **Clean Architecture** and **modularization** best practices.

```
Weather/
├── app/                     # Entry point: WeatherApp, MainActivity, DI initialization, navigation
├── build-logic/             # Gradle build logic (convention plugins)
├── core/
│   ├── ui/                  # Compose theme, Material 3, shared UI components
│   ├── utils/               # General utilities, extensions
│   ├── location/            # FusedLocationProvider logic
│   └── test-utils/          # Testing utilities
├── feature/
│   ├── forecast/            # Main screen: current weather + forecast
│   ├── search/              # City and map search screen
│   └── settings/            # Settings, theme control, favorites
└── build.gradle(.kts)
```

**Inside each feature module:**
```
feature/weather/
├── data/        # DTOs, repository implementations, mappers
├── domain/      # Models, use cases, repository interfaces
├── presentation/# ViewModels, state holders, Compose UI
└── di/          # Koin module definitions
```

**Dependency direction:**
```
app → feature:* → core:* → external libraries
```

---

## 🧠 Core Technologies

| Category     | Stack                                              |
|--------------|----------------------------------------------------|
| UI           | Jetpack Compose (Material 3, Navigation3)          |
| Architecture | Clean Architecture + MVVM                          |
| DI           | [Koin 4.x](https://insert-koin.io/)                |
| Network      | Retrofit + Kotlinx Serialization                   |
| Async        | Kotlin Coroutines + Flow                           |
| Location     | FusedLocationProviderClient (Google Play Services) |
| Maps         | Google Maps SDK                                    |
| Weather Data | OpenWeather One Call API 3.0                       |
| Language     | Kotlin 2.2 (JVM target 21)                         |
| Build        | Gradle Kotlin DSL + TOML version catalog           |
| Min SDK      | 31 (Android 12)                                    |
| Testing      | JUnit Jupiter                                      |

---

## 🔐 API Keys

API keys are stored in a **non-tracked** `apikeys.properties` file:

```properties
OPEN_WEATHER_API_KEY=your_api_key_here
GOOGLE_MAPS_API_KEY=your_google_maps_key_here
```

---

## 🧭 MVP Features

### 🌤 Home (Weather)
- Shows current weather based on device location
- Displays hourly forecast (up to 8 hours)
- Displays daily forecast (up to 8 days)
- Supports switching between favorite cities

### 🔍 Search
- Search city by name
- Search by map (Google Maps)
- “Find Me” button to detect current position

### ⚙️ Settings
- Switch theme (Light / Dark / System)
- Change default city
- “About” section

---

## 🧰 Build Commands

Clean project:
```bash
./gradlew clean
```

Assemble debug build:
```bash
./gradlew assembleDebug
```

Assemble release build:
```bash
./gradlew assembleRelease
```

Refresh dependencies and rebuild:
```bash
./gradlew clean build --refresh-dependencies
```

---

## 🔗 APIs

### OpenWeather One Call 3.0
[Documentation](https://openweathermap.org/api/one-call-3)

Example request:
```
https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&appid={API_KEY}&units=metric
```

---

## Icon set

Icon set credit: Pascal Vleugels [OpenWeatherMap iconset on Dribbble](https://dribbble.com/shots/4276406-OpenWeatherMap-iconset)
---

## 🧩 Koin Modules

| Module | Description |
|---------|-------------|
| `appModule` | Navigation, entry point |
| `weatherModule` | Repository, UseCase, ViewModel |
| `searchModule` | Google Maps integration, search logic |
| `settingsModule` | Preferences, UI |
| `networkModule` | Retrofit + OkHttp |
| `locationModule` | FusedLocationRepository |

---

## 🧪 Roadmap

- [ ] Add Room for caching favorite cities  
- [ ] Add notifications for weather alerts  
- [ ] Add detailed 8-day forecast screen  
- [ ] Add Gradle scripts for automatic module creation  
- [ ] Add TOML and Gradle auto-formatting tasks

---

## 🧑‍💻 Author

**Andrey Merkulov**  
Android Developer • Kotlin
