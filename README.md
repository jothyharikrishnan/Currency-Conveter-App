### Currency Converter App

<img width="312" alt="Screenshot 2024-12-16 at 10 51 39 AM" src="https://github.com/user-attachments/assets/d9839d87-271e-4037-960d-d4e009352552" />

<img width="324" alt="Screenshot 2024-12-16 at 11 05 31 AM" src="https://github.com/user-attachments/assets/b66480de-2484-4ed3-81a0-f8af804909c8" />


#### Overview
The Currency Converter App is a modern Android application designed to provide real-time currency conversion. It utilizes a robust architecture, following the Clean Architecture principles, and adheres to the Model-View-Intent (MVI) design pattern. The app also supports both light and dark themes, ensuring a pleasant user experience across different environments.

#### Features
1. **Currency Conversion**: Convert amounts from one currency to another using up-to-date exchange rates.
2. **Currency List**: Browse and select from a comprehensive list of available currencies.
3. **Real-time Updates**: Fetch the latest exchange rates from a remote API.
4. **Theming**: Supports both light and dark themes for improved usability in different lighting conditions.
5. **Modern UI**: Built using Jetpack Compose for a dynamic and intuitive user interface.

#### Tech Stack
- **Kotlin**: Main programming language for the application.
- **Jetpack Compose**: For building declarative UIs.
- **Hilt**: Dependency Injection to manage dependencies efficiently.
- **Retrofit**: For making HTTP requests and handling API responses.
- **StateFlow**: For managing and observing state in ViewModels.
- **Material Design 3**: Ensures a cohesive and modern design language.
- **MVI Pattern**: Implements the Model-View-Intent pattern for a clear separation of concerns.
- **Clean Architecture**: Organizes the codebase into layers, enhancing maintainability and scalability.

#### Project Structure
```plaintext
com.apps.currency_exchange
├── data
│   ├── ExchangeRepositoryImpl.kt
│   ├── ExchangeDto.kt
│   ├── ExchangeApiService.kt
├── di
│   ├── CoreModule.kt
├── domain
│   ├── Currency.kt
│   ├── ConvertUserCase.kt
│   ├── ExchangeRepository.kt
├── presentation
│   ├── ExchangeViewModel.kt
│   ├── ExchangeScreen.kt
│   ├── MainActivity.kt
│   ├── exchange
│   │   ├── ExchangeState.kt
│   │   ├── ExchangeAction.kt
```

1. **data**: Manages data-related classes, including repositories, data transfer objects (DTOs), and API services.
   - **ExchangeRepositoryImpl.kt**: Implements the repository interface for fetching data.
   - **ExchangeDto.kt**: Represents the data transfer object for API responses.
   - **ExchangeApiService.kt**: Defines Retrofit API endpoints.

2. **di**: Handles dependency injection using Hilt.
   - **CoreModule.kt**: Provides dependencies required across the app.

3. **domain**: Contains business logic, use cases, and data models.
   - **Currency.kt**: Represents the currency data model.
   - **ConvertUserCase.kt**: Encapsulates the use case for currency conversion.
   - **ExchangeRepository.kt**: Interface for the repository.

4. **presentation**: Manages the UI components and state management.
   - **ExchangeViewModel.kt**: ViewModel for managing UI-related data and logic.
   - **ExchangeScreen.kt**: Composable for the exchange screen.
   - **MainActivity.kt**: Main activity hosting the Compose UI.
   - **exchange**: Contains state and action classes specific to the exchange feature.
     - **ExchangeState.kt**: Defines the state model for the exchange feature.
     - **ExchangeAction.kt**: Defines possible actions that can trigger state changes.

### Theming Support
The app supports both light and dark themes, enhancing the user experience by adapting to different lighting conditions and user preferences. This is achieved using Jetpack Compose's theming capabilities, ensuring a cohesive look and feel across the app.
