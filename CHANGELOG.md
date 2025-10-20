# Changelog - SweetSync

All notable changes to the SweetSync project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [2.0.2] - 2025-10-20 (11:45 AM IST) - UI/UX Theme Consistency & Google Branding

### ✨ Added

#### Visual Assets
- **Google Logo Vector Drawable** (`ic_google_logo.xml`)
  - Official Google "G" logo with brand colors
  - Multi-color SVG vector drawable (Blue, Green, Yellow, Red)
  - 24dp × 24dp optimized for button integration
  - Maintains Google brand guidelines

#### Design Resources
- Added comprehensive color palette matching HomeScreen theme
- Defined PrimaryBlue (#64B5F6), SecondaryTeal (#4DB6AC)
- Added BackgroundSurface, BackgroundElevated variants
- Included TextHint, TextOnPrimary, TextOnSurface for better text hierarchy
- Added CardBorder and CardShadow definitions

### 🎨 Changed

#### Theme Unification
- **Login/Signup Screen** completely redesigned to match HomeScreen theme
- Updated `AuthScreenColors` object with enhanced dark theme palette
- Background colors: Changed from `#1A1A1A` to darker `#0F0F0F`
- Card backgrounds: Updated from `#333333` to `#242424`
- Primary action color: Changed from cyan (#4DD0E1) to softer teal (#4DB6AC)

#### Button Styling
- **Sign In/Sign Up buttons**:
  - Container color: Teal cyan → SecondaryTeal (#4DB6AC)
  - Text color: Black → White for better contrast
  - Loading indicator: Now white color
- **Continue with Google button**:
  - Replaced emoji icon (🔍) with official Google logo
  - Icon size: 20dp with `Color.Unspecified` tint (preserves multicolor)
  - Enhanced visual consistency with Material Design 3
- **Continue to Sign In button**: Updated to SecondaryTeal with white text
- **Text buttons**: Updated link color to SecondaryTeal

#### Text Field Improvements
- **Focus border color**: Changed to SecondaryTeal
- **Unfocused border opacity**: Reduced from 0.5 to 0.3 for subtler appearance
- **Label colors**: Added focused/unfocused states for better UX
- Improved cursor color consistency (SecondaryTeal)

#### Visual Elements
- **App icon gradient**: Updated from old teal to SecondaryTeal → PrimaryBlue
- **Verification success gradient**: Refined color scheme with new palette
- **Email icon**: Changed to SecondaryTeal
- **Error dialog retry button**: Updated to SecondaryTeal with white text

### 🔧 Technical Changes

#### Files Modified
- `AuthScreen.kt`
  - Complete theme overhaul of AuthScreenColors object
  - Added 15+ color definitions matching HomeScreen
  - Updated all button colors and text field styling
  - Integrated Google logo drawable
  - Added `painterResource` import for logo loading
  - Enhanced gradient definitions for visual consistency

- `ic_google_logo.xml` (NEW)
  - Vector drawable with Google's official brand colors
  - Four-color path definition (Blue, Green, Yellow, Red)
  - Scalable vector graphics for all screen densities

### 🎯 User Experience

#### Visual Consistency
- Login/Signup screen now perfectly matches Home screen aesthetics
- Cohesive dark theme throughout the entire app
- Professional Google branding on authentication button
- Improved visual hierarchy with refined color palette

#### Design Language
- Darker, more modern appearance
- Better contrast ratios for accessibility
- Consistent use of SecondaryTeal across interactive elements
- Enhanced Material Design 3 implementation

### 📱 Design Highlights

#### Color Palette
- **Primary**: PrimaryBlue (#64B5F6), PrimaryLight (#90CAF9)
- **Secondary**: SecondaryTeal (#4DB6AC), SecondaryTealLight (#80CBC4)
- **Backgrounds**: #0F0F0F (Primary), #1A1A1A (Secondary), #242424 (Card)
- **Text**: #F5F5F5 (Primary), #B8B8B8 (Secondary), #808080 (Hint)
- **Status**: SuccessGreen (#66BB6A), ErrorRed (#EF5350), WarningOrange (#FFB74D)

---

## [2.0.1] - 2025-10-20 (02:26 AM IST) - Google Sign-In Integration

### ✨ Added

#### Authentication
- **Google Sign-In** support as an alternative authentication method
- One-tap Google authentication flow
- Automatic user profile creation for Google sign-in users
- Seamless integration with existing Firebase Authentication

#### New Dependencies
- `com.google.android.gms:play-services-auth:21.2.0` - Google Sign-In SDK

#### Files Modified
- `AuthRepository.kt`
  - Added `signInWithGoogle(idToken: String)` method
  - Automatic Firestore profile creation for new Google users
  - Google credential authentication with Firebase
  
- `AuthViewModel.kt`
  - Added `signInWithGoogle(idToken: String)` method
  - State management for Google sign-in flow
  - Session persistence for Google-authenticated users

- `AuthScreen.kt`
  - Added "Continue with Google" button with white design
  - Google Sign-In activity result launcher
  - "OR" divider between email and Google sign-in options
  - Material Design 3 styled Google button

#### Build & Configuration
- `app/build.gradle.kts`
  - Added Google Play Services Auth dependency
  - Configured release build signing with keystore
  - Created release signing configuration

#### Security & Keys
- Created production release keystore (`release-keystore.jks`)
- SHA-1 fingerprint generated for Firebase Console
- Release build signing configured for Google Sign-In

### 🔄 Changed

#### UI/UX Improvements
- Enhanced authentication screen with modern Google sign-in button
- Added visual separator between authentication methods
- Improved button hierarchy and spacing

#### Build Configuration
- Updated release build type with proper signing config
- Keystore management for production releases

### 🔒 Security

#### Release Signing
- Generated release keystore with RSA 2048-bit encryption
- Validity: 10,000 days (until March 2053)
- SHA-1 fingerprint: `2D:4F:5C:69:32:FF:43:60:74:AD:E0:37:1A:09:21:48:F1:82:EA:31`
- Keystore protected in `.gitignore`

#### Firebase Configuration
- SHA-1 fingerprint ready for Firebase Console registration
- Google Sign-In OAuth 2.0 client configuration
- Secure token-based authentication flow

### 📋 Setup Requirements

To use Google Sign-In, you need to:
1. Add the SHA-1 fingerprint to Firebase Console
2. Enable Google Sign-In in Firebase Authentication
3. Download updated `google-services.json`
4. For Play Store releases, add Play Console SHA-1 to Firebase

### 🎯 User Experience

Users can now sign in using:
- **Email/Password** (existing method)
- **Google Account** (new method - one-tap sign-in)

Both methods create and sync user profiles in Firestore automatically.

---

## [2.0.0] - 2025-10-19 - Firebase Migration

### 🔥 Major Changes
Complete migration from Supabase to Firebase backend infrastructure.

### ✨ Added

#### Database & Backend
- **Firebase Firestore** integration for NoSQL database operations
- **Firebase Authentication** for user management (Email/Password)
- **Firebase Analytics** support (ready to use)
- Offline data persistence with Firebase local cache
- Real-time data synchronization using Firestore snapshot listeners
- Auto-generated document IDs for readings

#### New Files & Components
- `FirebaseClient.kt` - Central Firebase instance management
- `DateTimeUtils.kt` - Timestamp formatting utilities with multiple display options

#### Features
- Real-time data updates without polling
- Better offline support with automatic sync
- Timestamp formatting utilities (relative time, date only, time only, etc.)
- Improved data validation through Firestore security rules

### 🔄 Changed

#### Data Models
- `BloodSugarReading.kt`
  - `timestamp`: Changed from `String` (ISO 8601) to `Long` (milliseconds)
  - `meal_context`: Changed from `MealContext` enum to `String` for Firebase compatibility
  - Added `@DocumentId` annotation for Firestore integration
  - Added `@PropertyName` annotations for field mapping
  - Added no-arg constructor required by Firebase
  - Added `getMealContextEnum()` helper method
  - Added default values for all fields

- `UserProfile.kt` (in AuthRepository)
  - `created_at`: Changed from `String` to `Long` (milliseconds)
  - Added no-arg constructor
  - Simplified structure for Firestore compatibility

#### Repository Layer
- `AuthRepository.kt` - **COMPLETELY REWRITTEN**
  - Replaced Supabase Auth with Firebase Authentication
  - New user registration with display name support
  - Automatic user profile creation in Firestore
  - Simplified authentication flow
  - Added password reset functionality
  - Better error handling with Result types
  - No more metadata parsing complexities

- `BloodSugarRepository.kt` - **COMPLETELY REWRITTEN**
  - Replaced Supabase Postgrest with Firestore queries
  - Implemented real-time data with `getReadingsFlow()`
  - Added CRUD operations (Create, Read, Update, Delete)
  - Date range query support
  - Statistics calculations (average, count)
  - Better error handling and user verification

#### Dependency Injection
- `DatabaseModule.kt`
  - Provides `FirebaseClient` instead of `SupabaseClient`
  - Updated dependency graph for Firebase

#### ViewModels
- `BloodSugarViewModel.kt`
  - Updated `addReading()` to use millisecond timestamps
  - Changed `LocalDateTime.now()` to `System.currentTimeMillis()`
  - Updated meal context handling (enum to String)

- `AuthViewModel.kt`
  - Updated `UserProfile` instantiation with `Long` timestamps
  - Changed default timestamp from `""` to `0L`

#### UI Screens
- `HomeScreen.kt`
  - Replaced timestamp parsing with `DateTimeUtils.formatDateTime()`
  - Updated meal context display (removed `.name` call on String)
  - Changed `reading.meal_context` to `reading.getMealContextEnum()` for color function
  - Improved timestamp formatting

- `ChartsScreen.kt`
  - Rewrote `parseDateTime()` to handle `Long` instead of `String`
  - Simplified timestamp parsing (removed multiple try-catch chains)
  - Updated all chart data processing for numeric timestamps
  - Optimized sorting using direct timestamp comparison

- `AddReadingScreen.kt`
  - Compatible with new data model (no changes needed)

#### Dependencies (build.gradle.kts)
- **Removed:**
  - `io.github.jan-tennert.supabase:postgrest-kt:2.6.0`
  - `io.github.jan-tennert.supabase:gotrue-kt`
  - `io.ktor:ktor-client-android:2.3.12`

- **Added:**
  - `com.google.firebase:firebase-bom:32.7.0` (BOM for version management)
  - `com.google.firebase:firebase-auth-ktx` (Authentication)
  - `com.google.firebase:firebase-firestore-ktx` (Database)
  - `com.google.firebase:firebase-analytics-ktx` (Analytics)
  - `org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1` (Coroutines support)
  - `com.google.gms:google-services:4.4.0` (Google Services plugin)

#### Build Configuration
- Root `build.gradle.kts`
  - Added Google Services classpath
  - Added Google Services plugin

- App `build.gradle.kts`
  - Applied Google Services plugin
  - Updated dependencies

#### Security & Configuration
- `.gitignore`
  - Added Firebase-specific ignores
  - Added security file patterns (`*.jks`, `*.keystore`)
  - Added environment file patterns (`.env`, `secrets.properties`)
  - Added `serviceAccountKey.json` for admin operations

### 🗑️ Removed

#### Deleted Files
- `SupabaseClient.kt` - Replaced by `FirebaseClient.kt`
- Old Supabase configuration and hardcoded credentials

#### Removed Dependencies
- All Supabase-related libraries
- Ktor client (no longer needed)

#### Removed Code
- Supabase authentication logic
- Postgrest query builders
- Supabase metadata handling
- Complex timestamp parsing for ISO 8601 strings
- Redundant error handling for string-based timestamps

### 🐛 Fixed

#### Build Errors
- **ChartsScreen.kt** (19 errors)
  - Fixed argument type mismatches (String vs Long for timestamps)
  - Fixed CharSequence type errors
  - Simplified date parsing logic
  - Removed redundant format conversion attempts

- **HomeScreen.kt** (4 errors)
  - Fixed meal context type handling (enum vs String)
  - Fixed timestamp display formatting
  - Resolved `.name` call on String issue
  - Updated color function parameter types

#### Data Handling
- Timestamp consistency across the app
- Meal context enum/string conversion
- Empty notes handling (null vs "null" string)
- Real-time data synchronization issues

### 🔒 Security

#### Improvements
- Implemented Firestore Security Rules (user-level data isolation)
- Required authentication for all database operations
- Data validation at database level (glucose range, meal context values)
- Proper handling of user_id verification
- No hardcoded credentials in source code

#### Documentation
- Created comprehensive security guide
- Documented API key restriction process
- Added git history cleanup instructions
- Provided monitoring setup guidelines

#### Configuration
- Enhanced `.gitignore` with security patterns
- Protected Firebase service account keys
- Separated configuration from code

### 📚 Documentation

#### New Documentation Files
1. **SECURITY_FIX_GUIDE.md** - Security best practices and fixes
2. **CHANGELOG.md** - This file
3. Various migration guides (removed after migration complete)

#### Updated Documentation
- README.md - Updated with Firebase instructions
- Inline code documentation improved
- Better error messages and logging

### 🔧 Technical Improvements

#### Performance
- Faster queries with Firestore indexes
- Better offline performance with local cache
- Reduced network calls with real-time listeners
- Optimized batch operations

#### Architecture
- Cleaner separation of concerns
- Better error handling with Kotlin Result types
- Improved dependency injection
- More maintainable code structure

#### Data Format
- Standardized on millisecond timestamps (Long)
- Consistent string handling for enums
- Proper null handling throughout
- Type-safe data models

### 📊 Statistics

#### Files Changed
- **Created:** 3 new files (FirebaseClient, DateTimeUtils, Security Guide)
- **Modified:** 8 core files (repositories, viewmodels, screens)
- **Deleted:** 1 file (SupabaseClient)
- **Total lines changed:** ~1500+ lines

#### Dependencies
- **Removed:** 3 Supabase libraries
- **Added:** 5 Firebase libraries
- **Updated:** 2 build configuration files

#### Migration Scope
- **7 users** ready for migration
- **115 blood sugar readings** ready for migration
- **Date range:** 2021-2025 (4+ years of data)

---

## [1.0.0] - 2025-06-09 - Initial Release (Supabase)

### ✨ Added

#### Core Features
- Blood sugar tracking with glucose level input
- Before/After meal context selection
- Notes support for each reading
- User authentication (Email/Password)
- User profile management
- Charts and analytics visualization
- Historical data viewing

#### UI Components
- Material Design 3 theming (Dark theme)
- Home screen with reading list
- Add reading screen with date/time picker
- Charts screen with multiple visualizations
- Authentication screens (Sign up/Sign in)
- Pull-to-refresh functionality
- Swipe gestures support

#### Technical Implementation
- Supabase backend integration
- Supabase Auth for authentication
- Supabase Postgrest for database
- Hilt dependency injection
- Jetpack Compose UI
- MPAndroidChart for visualizations
- Kotlin Coroutines for async operations
- Material Design components

#### Data Features
- Automatic timestamp recording
- Data persistence
- User-specific data isolation
- Row Level Security (RLS) in Supabase

### 🎨 Design
- Dark theme with custom color scheme
- Gradient backgrounds
- Rounded corners and elevation
- Custom icons and illustrations
- Responsive layouts

### 🔧 Technical Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Backend:** Supabase
- **Database:** PostgreSQL (via Supabase)
- **Authentication:** Supabase Auth
- **Charts:** MPAndroidChart
- **DI:** Hilt
- **Build System:** Gradle (Kotlin DSL)

---

## Migration Impact Summary

### Breaking Changes
- Users must create new Firebase accounts (passwords don't transfer)
- Data model changes require app update
- Old API endpoints deprecated

### Data Migration
- All user profiles preserved
- All blood sugar readings preserved
- Timestamps converted to milliseconds
- Meal context preserved as strings
- User relationships maintained

### App Compatibility
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 35
- Compile SDK: 35
- Kotlin: 2.0.0
- Compose: BOM 2024.09.03

---

## Future Roadmap

### Planned Features
- [ ] Firebase Cloud Messaging (Push notifications)
- [ ] Firebase Crashlytics (Crash reporting)
- [ ] Firebase Performance Monitoring
- [ ] Firebase Remote Config (Feature flags)
- [ ] Data export functionality
- [ ] Medication tracking
- [ ] Meal logging with photos
- [ ] A1C estimation
- [ ] Sharing data with healthcare providers
- [ ] Multi-language support
- [ ] Insulin dose tracking
- [ ] Carbohydrate counting

### Technical Improvements
- [ ] Unit tests with Firebase Test Lab
- [ ] Integration tests
- [ ] UI tests
- [ ] Continuous Integration/Deployment
- [ ] Code coverage reporting
- [ ] Performance optimization
- [ ] Accessibility improvements

---

## Version History

| Version | Date | Description |
|---------|------|-------------|
| 2.0.2 | 2025-10-20 | UI/UX Theme Consistency & Google signin bug fix |
| 2.0.1 | 2025-10-20 | Google Sign-In integration with release signing |
| 2.0_fs | 2025-10-19 | Firebase Migration - Complete backend rewrite |
| 1.0.0 | 2025-06-09 | Initial release with Supabase backend |

---

**Note:** This changelog documents the major Firebase migration completed on October 19, 2025. All changes are backward-incompatible with the Supabase version and require a fresh installation.

