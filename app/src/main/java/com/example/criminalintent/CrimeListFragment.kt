package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
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

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
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
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
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
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
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
            holder.apply {
                titleTextView.text = crime.title
                dateTextView.text = crime.date.toString()
            }
        }

        /**
         * When the recycler view needs to know how many items are in the data set
         * backing it (such as when the recycler view first spins up), it will ask its
         * adapter by calling Adapter.getItemCount(). Here, getItemCount() returns the
         * number of items in the list of crimes to answer the recycler view’s request.
         */
        override fun getItemCount() = crimes.size

    }
}