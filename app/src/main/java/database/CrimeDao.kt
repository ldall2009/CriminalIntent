package database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.criminalintent.Crime
import java.util.*

/**
 * The first step to interacting with your database tables is to create a DAO.  a DAO is an
 * interface that contains functions from each database operation you want to perform.
 *
 * The @Dao annotation lets Room know that CrimeDao is one of your data access objects.  When
 * you hook CrimeDao up to your database class, Room will generate implementations of the
 * functions you add to this interface.
 */
@Dao
interface CrimeDao {

    /**
     * The @Query annotation indicates that a method is meant to pull information out of the
     * database, rather than inserting, updating, or deleting items from the database.  The
     * @Query annotation expects a string containing a SQL command as input.
     */
    @Query("SELECT * FROM crime")
            /**
             * By returning an instance of LiveData from your DAO class, you signal Room
             * to run your query on a background thread.  When the query completes, the
             * LiveData object will handle sending the crime data over to the main thread
             * and notify any observers.
             */
    fun getCrimes(): LiveData<List<Crime>>

    /**
     * The "WHERE id=(:id)" section asks Room to pull all columns from only the row whose id
     * matches the ID value provided "(:id)".
     */
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    @Update
    fun updateCrime(crime: Crime)

    @Insert
    fun addCrime(crime: Crime)

    @Delete
    fun deleteCrime(crime: Crime)
}