package com.example.criminalintent

import java.util.Date
import java.util.UUID

/**
 * fields that represent the crime's ID, title, date, and status, and a constructor
 * that initializes the ID and date fields.
 */
data class Crime(val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)