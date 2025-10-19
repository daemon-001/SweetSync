# SweetSync 🩸

<div align="center">
  <img src="logo.png" alt="SweetSync Logo" width="200" height="200">
  
  **A comprehensive Android app for diabetes management with interactive graphs, health metrics, and smart tracking.**
  
  [![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
  [![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
  [![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
</div>

## Overview

SweetSync is a modern Android application designed to help users manage their diabetes by tracking blood sugar readings, visualizing health trends, and providing insights through interactive charts and analytics. Built with the latest Android technologies and powered by Firebase, it offers a seamless experience for monitoring glucose levels, real-time data synchronization, and understanding patterns in your health data.

> **Note:** Version 2.0.0 (October 2025) - Complete Firebase migration with enhanced features, better offline support, and real-time data sync!

## Features

### **Blood Sugar Tracking**
- Log blood glucose readings with timestamps
- Track readings before and after meals
- Add personal notes to each reading
- View comprehensive reading history
- Monitor trends over time

### **Interactive Analytics**
- Beautiful, interactive charts and graphs
- Daily, weekly, and monthly trend analysis
- Quick statistics dashboard
- Color-coded glucose level indicators
- Export capabilities for healthcare providers

### **Secure Data Management**
- Firebase Authentication for secure user management
- Cloud Firestore for real-time data synchronization
- Built-in offline persistence and automatic sync
- Firestore Security Rules for data privacy
- Automatic conflict resolution and data backup

### **Modern UI/UX**
- Dark theme with custom color palette
- Material 3 design components
- Intuitive navigation and user flow
- Responsive design for all screen sizes
- Smooth animations and transitions

### **Health Insights**
- Average glucose level calculations
- Normal, high, and low reading counts
- Meal context analysis
- Trend identification and alerts
- Progress tracking over time

## Architecture

SweetSync follows modern Android development best practices with a clean, maintainable architecture:

### **MVVM Pattern**
- **Model**: Data models and repositories
- **View**: Jetpack Compose UI components
- **ViewModel**: Business logic and state management

### **Key Components**
- **Repository Pattern**: Centralized data management
- **Dependency Injection**: Hilt for clean architecture
- **Navigation Component**: Seamless screen navigation
- **State Management**: Compose state and ViewModels

## Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Kotlin | 2.0.0 |
| **UI Framework** | Jetpack Compose | BOM 2024.09.03 |
| **Architecture** | MVVM + Repository | - |
| **Dependency Injection** | Hilt | 2.48 |
| **Backend** | Firebase | BOM 32.7.0 |
| **Database** | Cloud Firestore | Latest |
| **Authentication** | Firebase Auth | Latest |
| **Navigation** | Navigation Compose | 2.8.2 |
| **Charts** | MPAndroidChart | 3.1.0 |
| **Serialization** | Kotlinx Serialization | 1.7.3 |
| **Build System** | Gradle (Kotlin DSL) | 8.12.3 |

## Screenshots

<div align="center">
  <img width="3697" height="1413" alt="thumbnail" src="https://github.com/user-attachments/assets/6ecd0245-8b75-479a-9ad9-4a44e48deec0" />
</div>

## Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox or later
- **Android SDK**: API 26+ (Android 8.0)
- **Kotlin**: 1.8+
- **Gradle**: 8.0+
- **Java**: 11+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/daemon-001/SweetSync.git
   cd SweetSync
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Open the project folder
   - Wait for Gradle sync to complete

3. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Firebase Authentication (Email/Password)
   - Create a Cloud Firestore database
   - Download `google-services.json` and place in `app/` directory
   - Set up Firestore Security Rules (see Security Setup below)

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio
   - Or run from command line: `./gradlew assembleDebug`

## 🗄️ Firebase Setup

### Firebase Configuration

1. **Create Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Click "Add Project" and follow the wizard
   - Select your project

2. **Register Android App**
   - Click Android icon to add Android app
   - Package name: `com.daemon.sweetsync`
   - Download `google-services.json`
   - Place it in `app/` directory

3. **Enable Authentication**
   - Go to Authentication → Sign-in method
   - Enable "Email/Password" provider
   - Save changes

4. **Create Firestore Database**
   - Go to Firestore Database
   - Click "Create database"
   - Choose production mode
   - Select your preferred location

5. **Set up Security Rules**
   Copy the following rules to Firestore → Rules:

   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       // Helper functions
       function isAuthenticated() {
         return request.auth != null;
       }
       
       function isOwner(userId) {
         return request.auth.uid == userId;
       }
       
       // User profiles
       match /user_profiles/{userId} {
         allow read, write: if isAuthenticated() && isOwner(userId);
       }
       
       // Blood sugar readings
       match /blood_sugar_readings/{readingId} {
         allow read: if isAuthenticated() && isOwner(resource.data.user_id);
         allow create: if isAuthenticated() && isOwner(request.resource.data.user_id);
         allow update, delete: if isAuthenticated() && isOwner(resource.data.user_id);
       }
     }
   }
   ```

6. **Create Firestore Indexes**
   Create these composite indexes in Firestore → Indexes:

   **Index 1:**
   - Collection: `blood_sugar_readings`
   - Fields: `user_id` (Ascending), `timestamp` (Descending)

   **Index 2:**
   - Collection: `blood_sugar_readings`
   - Fields: `user_id` (Ascending), `timestamp` (Ascending)

## Data Models

### BloodSugarReading
```kotlin
@Serializable
data class BloodSugarReading(
    @DocumentId
    val id: String? = null,
    
    @PropertyName("user_id")
    val user_id: String = "",
    
    @PropertyName("glucose_level")
    val glucose_level: Double = 0.0,
    
    @PropertyName("timestamp")
    val timestamp: Long = 0L, // Milliseconds since epoch
    
    @PropertyName("notes")
    val notes: String? = null,
    
    @PropertyName("meal_context")
    val meal_context: String = MealContext.BEFORE_MEAL.name
) {
    constructor() : this(null, "", 0.0, 0L, null, MealContext.BEFORE_MEAL.name)
    
    fun getMealContextEnum(): MealContext {
        return MealContext.valueOf(meal_context)
    }
}

@Serializable
enum class MealContext {
    @SerialName("BEFORE_MEAL")
    BEFORE_MEAL,
    
    @SerialName("AFTER_MEAL")
    AFTER_MEAL
}
```

### UserProfile
```kotlin
@Serializable
data class UserProfile(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val created_at: Long = 0L
) {
    constructor() : this("", "", "", 0L)
}
```

## Project Structure

```
app/src/main/java/com/daemon/sweetsync/
├── data/
│   ├── model/                    # Data models
│   │   └── BloodSugarReading.kt
│   └── repository/               # Data repositories
│       ├── AuthRepository.kt
│       ├── BloodSugarRepository.kt
│       └── FirebaseClient.kt
├── di/                          # Dependency injection
│   └── DatabaseModule.kt
├── presentation/
│   ├── navigation/              # Navigation components
│   │   └── SweetSyncNavigation.kt
│   ├── screen/                  # UI screens
│   │   ├── AddReadingScreen.kt
│   │   ├── AuthScreen.kt
│   │   ├── ChartsScreen.kt
│   │   └── HomeScreen.kt
│   └── viewmodel/               # ViewModels
│       ├── AuthViewModel.kt
│       └── BloodSugarViewModel.kt
├── ui/theme/                    # UI themes and styling
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
├── utils/                       # Utility classes
│   └── DateTimeUtils.kt
├── MainActivity.kt              # Main activity
└── SweetSyncApplication.kt      # Application class
```

## Security Features

- **Authentication**: Secure user login with Firebase Authentication
- **Data Privacy**: Firestore Security Rules ensure users only access their own data
- **Encryption**: All data transmitted over HTTPS/TLS
- **Offline Security**: Data cached securely with Firebase offline persistence
- **Session Management**: Automatic token refresh and secure logout
- **Data Validation**: Server-side validation through security rules (glucose range, meal context)
- **API Key Protection**: Restricted API keys prevent unauthorized access

## Testing

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

### Test Coverage

- Unit tests for ViewModels
- Integration tests for repository layer
- UI tests for critical user flows
- End-to-end tests for complete user journeys

## Building

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### APK Location
Generated APKs can be found in:
```
app/build/outputs/apk/debug/
app/build/outputs/apk/release/
```

## 🆘 Support

If you encounter any issues or have questions:

1. **Check the Issues** - Look through [existing issues](https://github.com/daemon-001/SweetSync/issues)
2. **Create a New Issue** - Provide detailed information about the problem
3. **Include Device Info** - Android version, device model, app version
4. **Attach Logs** - Include relevant error logs if possible

## 📊 Version History

| Version | Date | Description |
|---------|------|-------------|
| 2.0.0 | October 2025 | Firebase migration with real-time sync & offline support |
| 1.0.0 | June 2025 | Initial release with Supabase backend |

See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

## 📞 Contact

- **Developer**: [daemon-001](https://github.com/daemon-001)
- **Email**: [nitesh.kumar4work@gmail.com](mailto:nitesh.kumar4work@gmail.com)
- **LinkedIn**: [linkedin.com/in/daemon001](https://www.linkedin.com/in/daemon001/)

---

<div align="center">
  <strong>Empowering health through technology</strong>
  
  <p>Help us improve SweetSync by contributing, reporting bugs, or suggesting new features!</p>
</div>
