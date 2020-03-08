package com.pranjaldesai.coronavirustracker.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import java.lang.reflect.Type

private const val DEFAULT_PREFERENCE_LIBRARY_NAME: String = "Coronavirus_preferences"
const val PREFERENCE_REPORT_SORT_SELECTED = "countrySort"

class CoreSharedPreferences(val context: Context, val gson: Gson) {

    private val preferenceLibraryName: String = DEFAULT_PREFERENCE_LIBRARY_NAME

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(preferenceLibraryName, Context.MODE_PRIVATE)

    private val writablePreferences: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    fun clearAllSharedPreferences() = writablePreferences.clear().apply()

    private fun writeValue(key: String, value: Boolean) =
        writablePreferences.putBoolean(key, value).apply()

    private fun writeValue(key: String, value: Float) =
        writablePreferences.putFloat(key, value).apply()

    private fun writeValue(key: String, value: Int) = writablePreferences.putInt(key, value).apply()
    private fun writeValue(key: String, value: Long) =
        writablePreferences.putLong(key, value).apply()

    private fun writeValue(key: String, value: Set<String>) =
        writablePreferences.putStringSet(key, value).apply()

    private fun writeValue(key: String, value: String) =
        writablePreferences.putString(key, value).apply()

    private fun <T> writeValue(key: String, value: T) = writeValue(key, this.gson.toJson(value))

    private fun readValue(key: String, defaultValue: String) =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

    private fun readValue(key: String, defaultValue: Int) =
        sharedPreferences.getInt(key, defaultValue)

    private fun readValue(key: String, defaultValue: Float) =
        sharedPreferences.getFloat(key, defaultValue)

    private fun readValue(key: String, defaultValue: Long) =
        sharedPreferences.getLong(key, defaultValue)

    private fun readValue(key: String, defaultValue: Boolean) =
        sharedPreferences.getBoolean(key, defaultValue)

    private fun readValue(key: String, defaultValue: Set<String>) =
        sharedPreferences.getStringSet(key, defaultValue)

    private fun <T> readValue(key: String, defaultValue: T, cls: Class<T>): T =
        gson.fromJson(readValue(key, EMPTY_STRING), cls)?.let { it } ?: defaultValue

    private fun <T> readValue(key: String, type: Type): ArrayList<T> {
        val json = readValue(key, EMPTY_STRING)
        return gson.fromJson(json, type) ?: arrayListOf()
    }

    var countrySelectedSortStyle: ListSortStyle
        get() = readValue(
            PREFERENCE_REPORT_SORT_SELECTED,
            ListSortStyle.ALPHABETICAL_AZ,
            ListSortStyle::class.java
        )
        set(value) = writeValue(PREFERENCE_REPORT_SORT_SELECTED, value)
}