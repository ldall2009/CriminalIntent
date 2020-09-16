package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.Crime

/**
 * The @Database annotation tells Room that this class represents a database in your app.
 * The annotation itself requires two parameters. The first parameter is a list of entity
 * classes, which tells Room which entity classes to use when creating and managing tables
 * for this database. In this case, you only pass the Crime class, since it is the only
 * entity in the app.
 *
 * The second parameter is the version of the database. When you first create a database,
 * the version should be 1. As you develop your app in the future, you may add new entities
 * and new properties to existing entities. When this happens, you will need to modify your
 * entities list and increment your database version to tell Room something has changed.
 *
 * In order to have our database use the converter functions we created in CrimeTypeConverters.kt,
 * we must explicitly add the converters to this database class.  By adding the @TypeConverters
 * annotation and passing in your CrimeTypeConverters class, you tell your database to use the
 * functions in that class when converting your types.
 */
@Database(entities = [ Crime::class ], version=1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

    /**
     * used to register the DAO class with the database class.  This function hooks up our DAO.
     * Now, when the database is created, Room will generate a concrete implementation of the DAO
     * that you can access.  Once you have a reference to the DAO, you can call any of the
     * functions defined on it to interact with your database.
     */
    abstract fun crimeDao(): CrimeDao
}