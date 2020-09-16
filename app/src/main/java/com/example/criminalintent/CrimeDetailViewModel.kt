package com.example.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDetailViewModel : ViewModel() {

    /**
     * The crimeRepository property stores a handle to the CrimeRepository. This is
     * not necessary, but later on CrimeDetailViewModel will communicate with the
     * repository in more than one place, so the property will prove useful at that point.
     */
    private val crimeRepository = CrimeRepository.get()

    /**
     * crimeIdLiveData stores the ID of the crime currently displayed (or about to be
     * displayed) by CrimeFragment. When CrimeDetailViewModel is first created, the crime
     * ID is not set. Eventually, CrimeFragment will call
     * CrimeDetailViewModel.loadCrime(UUID) to let the ViewModel know which crime it needs
     * to load.
     */
    private val crimeIdLiveData = MutableLiveData<UUID>()

    /**
     * Note that you explicitly defined crimeLiveData’s type as LiveData<Crime?>.
     * Since crimeLiveData is publicly exposed, you should ensure it is not exposed as
     * a MutableLiveData. In general, ViewModels should never expose MutableLiveData.
     *
     * A live data transformation is a way to set up a trigger-response relationship
     * between two LiveData objects. A transformation function takes two inputs: a
     * LiveData object used as a trigger and a mapping function that must return a
     * LiveData object. The transformation function returns a new LiveData object, which
     * we call the transformation result, whose value gets updated every time a new value
     * gets set on the trigger LiveData instance.
     *
     * The transformation result’s value is calculated by executing the mapping function.
     * The value property on the LiveData returned from the mapping function is used to set
     * the value property on the live data transformation result.
     *
     * Using a transformation this way means the CrimeFragment only has to observe the
     * exposed CrimeDetailViewModel.crimeLiveData one time. When the fragment changes the
     * ID it wants to display, the ViewModel just publishes the new crime data to the
     * existing live data stream.
     */
    var crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) { crimeId ->
            crimeRepository.getCrime(crimeId)
        }

    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}