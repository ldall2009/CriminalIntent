package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        /**
         * Used to send the new date the user selected for a crime back to CrimeFragment.
         *
         * The OnDateSetListener is used to receive the date the user selects. The first
         * parameter is for the DatePicker the result is coming from. Since you are not
         * using that parameter in this case, you name it _. This is a Kotlin convention
         * to denote parameters that are unused.
         *
         * The selected date is provided in year, month, and day format, but you need a Date
         * to send back to CrimeFragment. You pass these values to the GregorianCalendar and
         * access the time property to get a Date object.
         *
         * Once you have the date, it needs to be sent back to CrimeFragment. The
         * targetFragment property stores the fragment instance that started your
         * DatePickerFragment. Since it is nullable, you wrap it in a safe-call let block.
         * You then cast the fragment instance to your Callbacks interface and call the
         * onDateSelected() function, passing in your new date.
         */
        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->

            val resultDate = GregorianCalendar(year, month, day).time

            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDate)
            }
        }

        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        /**
         * The DatePickerDialog constructor takes in several parameters. The first is a
         * context object, which is required to access the necessary resources for the
         * view. The second parameter is for the date listener. The last three parameters
         * are the year, month, and day that the date picker should be initialized to.
         */
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    /**
     * Used to pass data between two fragments that are hosted by the same activity --
     * CrimeFragment and DatePickerFragment.
     *
     * To get the Crime's date to DatePickerFragment, we use the newInstance(Date)
     * function and make the Date an argument on the fragment.
     *
     * Creating and setting fragment arguments is typically done in a newInstance(...)
     * function.
     */
    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }

    }
}