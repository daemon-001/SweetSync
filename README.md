# SweetSync 🩸

<div align="center">
  <img src="logo.png" alt="SweetSync Logo" width="200" height="200">
  
  **A comprehensive Android app for diabetes management with interactive graphs, health metrics, and smart tracking.**
  
  [![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
  [![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
  [![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
</div>

## Overview

SweetSync is a modern Android application designed to help users manage their diabetes by tracking blood sugar readings, visualizing health trends, and providing insights through interactive charts and analytics. Built with the latest Android technologies, it offers a seamless experience for monitoring glucose levels and understanding patterns in your health data.

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
- User authentication with Supabase
- Cloud-based data synchronization
- Local offline storage with Room database
- Row-level security for data privacy
- Automatic data backup and sync

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
| **Language** | Kotlin | 1.8+ |
| **UI Framework** | Jetpack Compose | Latest |
| **Architecture** | MVVM + Repository | - |
| **Dependency Injection** | Hilt | 2.48 |
| **Backend** | Supabase | Latest |
| **Local Database** | Room | Latest |
| **Navigation** | Navigation Compose | 2.8.2 |
| **Charts** | MPAndroidChart | 3.1.0 |
| **Serialization** | Kotlinx Serialization | 1.7.3 |
| **Build System** | Gradle (Kotlin DSL) | 8.0+ |

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

3. **Configure Supabase**
   - Create a Supabase project at [supabase.com](https://supabase.com)
   - Set up authentication (email/password)
   - Create the database schema (see Database Setup below)
   - Update configuration in `SupabaseClient.kt`

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio
   - Or run from command line: `./gradlew assembleDebug`

## 🗄️ Database Setup

### Supabase Configuration

1. **Create Tables**
   Run the following SQL in your Supabase SQL Editor:

   ```sql
   -- Create user_profiles table
   CREATE TABLE user_profiles (
       id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
       email TEXT NOT NULL UNIQUE,
       name TEXT NOT NULL,
       created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
       updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
   );

   -- Create meal_context enum type
   CREATE TYPE meal_context AS ENUM ('BEFORE_MEAL', 'AFTER_MEAL');

   -- Create blood_sugar_readings table
   CREATE TABLE blood_sugar_readings (
       id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
       user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
       glucose_level DECIMAL(5,2) NOT NULL CHECK (glucose_level > 0 AND glucose_level < 1000),
       timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
       notes TEXT,
       meal_context meal_context NOT NULL,
       created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
       updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
   );
   ```

2. **Set up Row Level Security (RLS)**
   ```sql
   -- Enable RLS
   ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;
   ALTER TABLE blood_sugar_readings ENABLE ROW LEVEL SECURITY;

   -- Create policies for user_profiles
   CREATE POLICY "Users can view their own profile" ON user_profiles
   FOR SELECT USING (auth.uid() = id);

   CREATE POLICY "Users can update their own profile" ON user_profiles
   FOR UPDATE USING (auth.uid() = id);

   CREATE POLICY "Users can insert their own profile" ON user_profiles
   FOR INSERT WITH CHECK (auth.uid() = id);

   -- Create policies for blood_sugar_readings
   CREATE POLICY "Users can view their own readings" ON blood_sugar_readings
   FOR SELECT USING (auth.uid() = user_id);

   CREATE POLICY "Users can insert their own readings" ON blood_sugar_readings
   FOR INSERT WITH CHECK (auth.uid() = user_id);

   CREATE POLICY "Users can update their own readings" ON blood_sugar_readings
   FOR UPDATE USING (auth.uid() = user_id);

   CREATE POLICY "Users can delete their own readings" ON blood_sugar_readings
   FOR DELETE USING (auth.uid() = user_id);
   ```

3. **Update Configuration**
   Update the Supabase configuration in `app/src/main/java/com/daemon/sweetsync/data/repository/SupabaseClient.kt`:

   ```kotlin
   val client = createSupabaseClient(
       supabaseUrl = "YOUR_SUPABASE_URL",
       supabaseKey = "YOUR_SUPABASE_ANON_KEY"
   ) {
       install(Auth)
       install(Postgrest)
   }
   ```

## Data Models

### BloodSugarReading
```kotlin
@Serializable
data class BloodSugarReading(
    val id: String? = null,
    val user_id: String,
    val glucose_level: Double,
    val timestamp: String,
    val notes: String? = null,
    val meal_context: MealContext
)

@Serializable
enum class MealContext {
    @SerialName("BEFORE_MEAL")
    BEFORE_MEAL,
    
    @SerialName("AFTER_MEAL")
    AFTER_MEAL
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
│       └── SupabaseClient.kt
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
├── MainActivity.kt              # Main activity
└── SweetSyncApplication.kt      # Application class
```

## Security Features

- **Authentication**: Secure user login with Supabase Auth
- **Data Privacy**: Row-level security ensures users only access their own data
- **Encryption**: All data transmitted over HTTPS
- **Local Storage**: Sensitive data cached securely on device
- **Session Management**: Automatic token refresh and secure logout

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

## 📞 Contact

- **Developer**: [daemon-001](https://github.com/daemon-001)
- **Email**: [nitesh.kumar4work@gmail.com](mailto:nitesh.kumar4work@gmail.com)
- **LinkedIn**: [linkedin.com/in/daemon001](https://www.linkedin.com/in/daemon001/)

---

<div align="center">
  <strong>Empowering health through technology</strong>
  
  <p>Help us improve SweetSync by contributing, reporting bugs, or suggesting new features!</p>
  
  [![GitHub stars](https://img.shields.io/github/stars/daemon-001/SweetSync?style=social)](https://github.com/daemon-001/SweetSync/stargazers)
  [![GitHub forks](https://img.shields.io/github/forks/daemon-001/SweetSync?style=social)](https://github.com/daemon-001/SweetSync/network/members)
  [![GitHub issues](https://img.shields.io/github/issues/daemon-001/SweetSync)](https://github.com/daemon-001/SweetSync/issues)
</div>
