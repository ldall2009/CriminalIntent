package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        // retrieve the UUID from the fragment arguments
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID

        // load crime from database
        crimeDetailViewModel.loadCrime(crimeId)
    }

    /**
     * This is where you create and configure the fragment's view.  This function is
     * where you inflate the layout for the fragment's view and return the inflated
     * View to the host activity.  The LayoutInflater and ViewGroup parameters are
     * necessary to inflate the layout.  The Bundle will contain data that this
     * function can use to re-create the view from a saved state.
     *
     * Within this function, you explicitly inflate the fragment's view by calling
     * LayoutInflater.inflate(...) and passing in the layout resource ID.  The second
     * parameter is your view's parent, which is usually needed to configure the
     * widgets properly.  The third parameter tells the layout inflater whether to
     * immediately add the inflated view to the view's parent.  You pass in false
     * because the fragment's view will be hosted in the activity's container view.
     * The fragment's view does not need to be added to the parent view immediately.
     *
     * Additionally, the onCreateView(...) function is the place to wire up widgets
     * such as the EditText, CheckBox, and Button widgets we have in our fragment_crime
     * layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        //After the view is inflated, get a reference to the EditText using findViewById.
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }


    override fun onStart(){
        super.onStart()

        /**
         * Setting listeners in a fragment works exactly the same as in an activity.
         * Here, you create an anonymous class that implements the verbose TextWatcher
         * interface.  TextWatcher has three functions, but you only care about one:
         * onTextChanged(...).
         */
        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            /**
             * In onTextChanged(...), you call toString() on the CharSequence that is
             * the user's input.  This function returns a string, which you then use
             * to set the Crime's title.
             */
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {

                /**
                 * To have CrimeFragment receive the date back from DatePickerFragment, you
                 * need a way to keep track of the relationship between the two fragments.
                 *
                 * You can create a similar connection by making CrimeFragment the target
                 * fragment of DatePickerFragment. This connection is automatically
                 * re-established after both CrimeFragment and DatePickerFragment are
                 * destroyed and re-created by the OS.
                 */
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)

                /**
                 * this@CrimeFragment is needed to call requireFragmentManager() from the
                 * CrimeFragment instead of the DatePickerFragment. this references the
                 * DatePickerFragment inside the apply block, so you need to specify the
                 * this from the outer scope.
                 */
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        /**
         * the data will get saved when the user finishes the detail screen or when the
         * user switches tasks
         */
        crimeDetailViewModel.saveCrime(crime)
    }

    // Used to allow CrimeFragment to respond to new dates entered for a crime by the user.
    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    /**
     * To attach the arguments bundle to a fragment, you call Fragment.setArguments(Bundle).
     * Attaching arguments to a fragment must be done after the fragment is created but
     * before it is added to an activity.
     *
     * To accomplish this, Android programmers follow a convention of adding a companion
     * object that contains the newInstance(...) function to the Fragment class. This
     * function creates the fragment instance and bundles up and sets its arguments.
     *
     * When the hosting activity needs an instance of that fragment, you have it call
     * the newInstance(...) function rather than calling the constructor directly. The
     * activity can pass in any required parameters to newInstance(...) that the fragment
     * needs to create its arguments.
     *
     * In CrimeFragment, we have a newInstance(UUID) function that accepts a UUID,
     * creates an arguments bundle, creates a fragment instance, and then attaches
     * the arguments to the fragment.
     */

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        solvedCheckBox. apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}