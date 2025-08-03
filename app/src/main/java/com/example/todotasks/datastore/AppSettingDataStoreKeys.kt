package com.example.todotasks.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppSettingDataStoreKeys{
    val IS_NOTIFICATION_ON = booleanPreferencesKey("is_notification_on")
}