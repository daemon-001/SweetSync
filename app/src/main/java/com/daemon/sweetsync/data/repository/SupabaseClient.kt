
package com.daemon.sweetsync.data.repository

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClient @Inject constructor() {
    val client = createSupabaseClient(
        supabaseUrl = "https://jmfkcxgkktlvfrulhehk.supabase.co", // Replace with your Supabase URL
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptZmtjeGdra3RsdmZydWxoZWhrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgyNTg1NDEsImV4cCI6MjA2MzgzNDU0MX0.G9ytLA6XEX3CGeOMQTK2XvouxNsu5AVohwlZFhvbioY" // Replace with your Supabase anon key
    ) {
        install(Auth)
        install(Postgrest)
    }
}




//Supabase sql querry

//-- SweetSync Database Schema for Supabase
//-- This SQL sets up the complete database structure for your blood sugar tracking app
//
//-- ====================================
//-- 1. Create user_profiles table
//-- ====================================
//CREATE TABLE user_profiles (
//id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
//email TEXT NOT NULL UNIQUE,
//name TEXT NOT NULL,
//created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
//updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
//);
//
//-- ====================================
//-- 2. Create meal_context enum type (skip if already exists)
//-- ====================================
//-- CREATE TYPE meal_context AS ENUM ('BEFORE_MEAL', 'AFTER_MEAL');
//-- Note: meal_context type already exists, skipping creation
//
//-- ====================================
//-- 3. Create blood_sugar_readings table
//-- ====================================
//CREATE TABLE blood_sugar_readings (
//id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
//user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,
//glucose_level DECIMAL(5,2) NOT NULL CHECK (glucose_level > 0 AND glucose_level < 1000),
//timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
//notes TEXT,
//meal_context meal_context NOT NULL,
//created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
//updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
//);
//
//-- ====================================
//-- 4. Create indexes for better performance
//-- ====================================
//CREATE INDEX idx_user_profiles_email ON user_profiles(email);
//CREATE INDEX idx_blood_sugar_readings_user_id ON blood_sugar_readings(user_id);
//CREATE INDEX idx_blood_sugar_readings_timestamp ON blood_sugar_readings(timestamp DESC);
//CREATE INDEX idx_blood_sugar_readings_user_timestamp ON blood_sugar_readings(user_id, timestamp DESC);
//
//-- ====================================
//-- 5. Create updated_at trigger function
//-- ====================================
//CREATE OR REPLACE FUNCTION update_updated_at_column()
//RETURNS TRIGGER AS $$
//BEGIN
//NEW.updated_at = NOW();
//RETURN NEW;
//END;
//$$ language 'plpgsql';
//
//-- ====================================
//-- 6. Create triggers for updated_at
//-- ====================================
//CREATE TRIGGER update_user_profiles_updated_at
//BEFORE UPDATE ON user_profiles
//FOR EACH ROW
//EXECUTE FUNCTION update_updated_at_column();
//
//CREATE TRIGGER update_blood_sugar_readings_updated_at
//BEFORE UPDATE ON blood_sugar_readings
//FOR EACH ROW
//EXECUTE FUNCTION update_updated_at_column();
//
//-- ====================================
//-- 7. Set up Row Level Security (RLS)
//-- ====================================
//
//-- Enable RLS on both tables
//ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;
//ALTER TABLE blood_sugar_readings ENABLE ROW LEVEL SECURITY;
//
//-- User profiles policies
//CREATE POLICY "Users can view their own profile" ON user_profiles
//FOR SELECT USING (auth.uid() = id);
//
//CREATE POLICY "Users can update their own profile" ON user_profiles
//FOR UPDATE USING (auth.uid() = id);
//
//CREATE POLICY "Users can insert their own profile" ON user_profiles
//FOR INSERT WITH CHECK (auth.uid() = id);
//
//-- Blood sugar readings policies
//CREATE POLICY "Users can view their own readings" ON blood_sugar_readings
//FOR SELECT USING (auth.uid() = user_id);
//
//CREATE POLICY "Users can insert their own readings" ON blood_sugar_readings
//FOR INSERT WITH CHECK (auth.uid() = user_id);
//
//CREATE POLICY "Users can update their own readings" ON blood_sugar_readings
//FOR UPDATE USING (auth.uid() = user_id);
//
//CREATE POLICY "Users can delete their own readings" ON blood_sugar_readings
//FOR DELETE USING (auth.uid() = user_id);
//
//-- ====================================
//-- 8. Create helpful views (optional)
//-- ====================================
//
//-- View for readings with user info (for admin purposes)
//CREATE VIEW readings_with_user AS
//SELECT
//bsr.id,
//bsr.glucose_level,
//bsr.timestamp,
//bsr.notes,
//bsr.meal_context,
//bsr.created_at,
//up.name as user_name,
//up.email as user_email
//FROM blood_sugar_readings bsr
//JOIN user_profiles up ON bsr.user_id = up.id;
//
//-- ====================================
//-- 9. Create functions for statistics (optional)
//-- ====================================
//
//-- Function to get user's average glucose level
//CREATE OR REPLACE FUNCTION get_user_average_glucose(user_uuid UUID)
//RETURNS DECIMAL AS $$
//BEGIN
//RETURN (
//SELECT COALESCE(AVG(glucose_level), 0)
//FROM blood_sugar_readings
//WHERE user_id = user_uuid
//);
//END;
//$$ LANGUAGE plpgsql SECURITY DEFINER;
//
//-- Function to get user's reading count
//CREATE OR REPLACE FUNCTION get_user_reading_count(user_uuid UUID)
//RETURNS INTEGER AS $$
//BEGIN
//RETURN (
//SELECT COUNT(*)::INTEGER
//FROM blood_sugar_readings
//WHERE user_id = user_uuid
//);
//END;
//$$ LANGUAGE plpgsql SECURITY DEFINER;
//
//-- ====================================
//-- 10. Sample data (for testing - remove in production)
//-- ====================================
//
//-- Note: This assumes you have test users in auth.users
//-- INSERT INTO user_profiles (id, email, name) VALUES
//-- ('123e4567-e89b-12d3-a456-426614174000', 'test@example.com', 'Test User');
//
//-- INSERT INTO blood_sugar_readings (user_id, glucose_level, timestamp, notes, meal_context) VALUES
//-- ('123e4567-e89b-12d3-a456-426614174000', 120.5, NOW() - INTERVAL '2 hours', 'Morning reading', 'BEFORE_MEAL'),
//-- ('123e4567-e89b-12d3-a456-426614174000', 140.0, NOW() - INTERVAL '1 hour', 'After breakfast', 'AFTER_MEAL');
//
//-- ====================================
//-- Notes for Implementation:
//-- ====================================
//
///*
//1. Run this SQL in your Supabase SQL Editor
//2. The schema matches your Kotlin data models exactly
//3. RLS policies ensure users can only access their own data
//4. Indexes are optimized for your app's query patterns
//5. The meal_context enum matches your Kotlin enum values
//6. Foreign key constraints ensure data integrity
//7. Triggers automatically update the updated_at timestamps
//
//Remember to:
//- Enable email confirmation in Supabase Auth settings if needed
//- Configure your Supabase client in your Android app
//- Test the RLS policies work correctly
//- Adjust the glucose_level CHECK constraint if needed (currently 0-1000 mg/dL)
//*/



