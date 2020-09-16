package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * fields that represent the crime's ID, title, date, and status, and a constructor
 * that initializes the ID and date fields.
 *
 * Room structures the database tables for your application based on the entities you define.
 * Entities are model classes you create, annotated with the @Entity annotation.  Room will
 * create a database table for any class with that annotation.
 *
 * Since you want to store crime objects in your database, Crime must be a Room entity.
 *
 * The @Entity annotiotion is applied at the class level.  This entity annotation indicates
 * that the class defines the structure of a table, or set of tables, in the database.  In
 * this case, each row in the table will represent an individual Crime.  Each property
 * defined on the class will be a column in the table, with the name of the property as
 * the name of the column.
 *
 * The @PrimaryKey annotation specifies which column in the database will be the primary key.
 * The primary key in a database is a column that holds data that is unique for each entry, or
 * row, so that it can be used to look up individual entries.
 */
@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)