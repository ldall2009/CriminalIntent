package com.example.criminalintent

import android.app.Application
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

/**
 * To do work as soon as your application is ready, you can create an Application subclass.
 * This allows you to access lifecycle information about the application itself. Create
 * a class called CriminalIntentApplication that extends Application and override
 * Application.onCreate() to set up the repository initialization.
 */
class CriminalIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}