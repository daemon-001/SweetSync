package com.daemon.sweetsync.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility functions for converting and formatting timestamps
 * between Firebase (Long milliseconds) and display formats
 */
object DateTimeUtils {
    
    /**
     * Format timestamp in milliseconds to readable date and time
     * Example: "Jan 15, 2024 at 10:30 AM"
     */
    fun formatDateTime(millis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    
    /**
     * Format timestamp to date only
     * Example: "Jan 15, 2024"
     */
    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    
    /**
     * Format timestamp to time only
     * Example: "10:30 AM"
     */
    fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    
    /**
     * Format timestamp to short date
     * Example: "15/01/2024"
     */
    fun formatShortDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    
    /**
     * Format timestamp for charts and graphs
     * Example: "Jan 15"
     */
    fun formatChartDate(millis: Long): String {
        val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    
    /**
     * Get relative time string
     * Example: "2 hours ago", "Yesterday", "Last week"
     */
    fun getRelativeTimeString(millis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - millis
        
        return when {
            diff < 60_000 -> "Just now" // Less than 1 minute
            diff < 3600_000 -> "${diff / 60_000} minutes ago" // Less than 1 hour
            diff < 86400_000 -> "${diff / 3600_000} hours ago" // Less than 1 day
            diff < 172800_000 -> "Yesterday" // Less than 2 days
            diff < 604800_000 -> "${diff / 86400_000} days ago" // Less than 1 week
            diff < 2592000_000 -> "${diff / 604800_000} weeks ago" // Less than 1 month
            else -> formatDate(millis) // More than 1 month, show full date
        }
    }
    
    /**
     * Check if timestamp is today
     */
    fun isToday(millis: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        
        calendar.timeInMillis = millis
        val timestampDay = calendar.get(Calendar.DAY_OF_YEAR)
        
        return today == timestampDay
    }
    
    /**
     * Get start of day timestamp (00:00:00)
     */
    fun getStartOfDay(millis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get end of day timestamp (23:59:59)
     */
    fun getEndOfDay(millis: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    /**
     * Get timestamp for N days ago
     */
    fun getDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return calendar.timeInMillis
    }
    
    /**
     * Convert LocalDateTime string to milliseconds (for migration purposes)
     * Example: "2024-01-15T10:30:00" -> 1705318200000
     */
    @Deprecated("Only for migration from Supabase")
    fun localDateTimeToMillis(dateTimeString: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.parse(dateTimeString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    
    /**
     * Convert ISO 8601 string to milliseconds (for Supabase migration)
     * Example: "2024-01-15T10:30:00Z" -> 1705318200000
     */
    @Deprecated("Only for migration from Supabase")
    fun iso8601ToMillis(iso8601String: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(iso8601String)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}

