package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import database.CrimeDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

/**
 * A repository class encapsulates the logic for accessing data from a single source or a
 * set of sources. It determines how to fetch and store a particular set of data, whether
 * locally in a database or from a remote server. Your UI code will request all the data
 * from the repository, because the UI does not care how the data is actually stored or
 * fetched. Those are implementation details of the repository itself.
 *
 * Since CriminalIntent is a simpler app, the repository will only handle fetching data
 * from the database.
 *
 * CrimeRepository is a singleton. This means there will only ever be one instance of it
 * in your app process.
 *
 * A singleton exists as long as the application stays in memory, so storing any properties
 * on the singleton will keep them available throughout any lifecycle changes in your
 * activities and fragments. Be careful with singleton classes, as they are destroyed when
 * Android removes your application from memory. The CrimeRepository singleton is not a
 * solution for long-term storage of data. Instead, it gives the app an owner for the crime
 * data and provides a way to easily pass that data between controller classes.
 */
class CrimeRepository private constructor(context: Context) {

    /**
     * Room.databaseBuilder() creates a concrete implementation of your abstract
     * CrimeDatabase using three parameters.  It first needs a Context object, since
     * the database is accessing the filesystem.  You pass in the application context
     * because the singleton will most likely live longer than any of your activity
     * classes.  The second parameter is the database class that you want Room to create.
     * The third is the name of the database file you want Room to create for you.
     */
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    /**
     * An Executor is an object that references a thread.  An executor instance has a
     * function called execute that accepts a block of code to run.  The code you provide
     * in the block will run on whatever thread the executor points to.
     *
     * The newSingleThreadExecutor() function returns an executor instance that points to a
     * new thread.  Any work you execute with the executor will therefore happen off the
     * main thread.
     */
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    fun deleteCrime(crime: Crime) {
        executor.execute {
            crimeDao.deleteCrime(crime)
        }
    }

    /**
     * To make CrimeRepository a singleton, you add two functions to its companion object.
     * One initializes a new instance of the repository, and the other accesses the
     * repository. You also mark the constructor as private to ensure no components can
     * go rogue and create their own instance.
     */
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        /**
         * The getter function is not very nice if you have not called initialize() before
         * it. It will throw an IllegalStateException, so you need to make sure that you
         * initialize your repository when your application is starting.
         */
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}