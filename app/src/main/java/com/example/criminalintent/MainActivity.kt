package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * When you need to retrieve the CrimeFragment from the FragmentManager,
         * you ask for it by container view ID:
         */
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            /**
             * The following statement creates and commits a fragment transaction.
             * Fragment transactions are used to add, remove, attach, detach, or replace
             * fragments in the fragment list.  They allow you to group multiple
             * operations together, such as adding multiple fragments to different
             * containers at the same time.  They are the heart of how you use fragments
             * to compose and recompose screens at runtime.
             *
             * The FragmentManager.beginTransaction() function creates and returns
             * an instance of FragmentTransaction.  The FragmentTransaction instead of
             * Unit, which allows you to chain them together.  So the code says "Create
             * a new fragment transaction, include on add operation in it, and then
             * commit it."
             *
             * The add(...)function is the meat of the transaction.  It has two parameters:
             * a container view ID and the newly created CrimeFragment.  The container
             * view ID should look familiar.  It is the resource ID of the FrameLayout
             * that you defined in activity_main.xml .
             */
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)

        /**
         * FragmentTransaction.replace(Int, Fragment) replaces the fragment hosted in the
         * activity (in the container with the integer resource ID specified) with the new
         * fragment provided. If a fragment is not already hosted in the container
         * specified, the new fragment is added, just as if you had called
         * FragmentTransaction.add(Int, fragment).
         *
         * When you add a transaction to the back stack, this means that when the user
         * presses the Back button the transaction will be reversed. So, in this case,
         * CrimeFragment will be replaced with CrimeListFragment.
         */
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("CrimeList")
            .commit()
    }
}