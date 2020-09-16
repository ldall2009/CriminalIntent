package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        /**
         * RecyclerView does not position items on the screen itself. It delegates that
         * job to the LayoutManager. The LayoutManager positions every item and also defines
         * how scrolling works.
         *
         * There are a few built-in LayoutManagers to choose from, and you can find more as
         * third-party libraries. You are using the LinearLayoutManager, which will position
         * the items in the list vertically.
         */
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * The LiveData.observe(LifecycleOwner, Observer) function is used to register an
         * observer on the LiveData instance and tie the life of the observation to the
         * life of another component, such as an activity or fragment.
         * The second parameter to the observe(...) function is an Observer implementation.
         * This object is responsible for reacting to new data from the LiveData.  In this
         * case, the observer's code block is executed whenever the LiveData's list of crimes
         * gets updated.  The observer receives a list of crimes from the LiveData and prints
         * a log statement if the property is not null
         */
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    /**
     * The recycler view is responsible for:
     * - asking the adapter to create a new ViewHolder.
     * - asking the adapter to bind a ViewHolder to the item from the backing data at a
     *   given position.
     *
     * The RecyclerView expects an item view to be wrapped in an instance of ViewHolder.
     * A ViewHolder stores a reference to an item’s view (and sometimes references to
     * specific widgets within that view).
     *
     * Define a view holder by adding an inner class in CrimeListFragment that extends from
     * RecyclerView.ViewHolder.
     *
     * In CrimeHolder’s constructor, you take in the view to hold on to. Immediately, you
     * pass it as the argument to the RecyclerView.ViewHolder constructor. The base
     * ViewHolder class will then hold on to the view in a property named itemView.
     *
     * The view holder stashes references to the title and date text views so you can
     * easily change the value displayed later without having to go back through the
     * item's view hierarchy.
     */
    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} clicked!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * The adapter is responsible for:
     * - creating the necessary ViewHolders when asked.
     * - binding ViewHolders to data from the model layer when asked.
     */
    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        /**
         * Adapter.onCreateViewHolder(...) is responsible for creating a view to display,
         * wrapping the view in a view holder, and returning the result. In this case,
         * you inflate list_item_view.xml and pass the resulting view to a new instance
         * of CrimeHolder.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        /**
         * Adapter.onBindViewHolder(holder: CrimeHolder, position: Int) is responsible for
         * populating a given holder with the crime from a given position. In this case,
         * you get the crime from the crime list at the requested position. You then use
         * the title and data from that crime to set the text in the corresponding text
         * views.
         */
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        /**
         * When the recycler view needs to know how many items are in the data set
         * backing it (such as when the recycler view first spins up), it will ask its
         * adapter by calling Adapter.getItemCount(). Here, getItemCount() returns the
         * number of items in the list of crimes to answer the recycler view’s request.
         */
        override fun getItemCount() = crimes.size

    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}