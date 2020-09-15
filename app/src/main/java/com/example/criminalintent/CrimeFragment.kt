package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
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
        val view =
            inflater.inflate(R.layout.fragment_crime, container, false)

        //After the view is inflated, get a reference to the EditText using findViewById.
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

            return view
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
            setOnCheckedChangeListener {
                _, isChecked -> crime.isSolved = isChecked
            }
        }
    }
}