package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import java.lang.Boolean.TRUE

class MainFragment : Fragment() {

    //create view model
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModelFactory(activity.application))
            .get(MainViewModel::class.java)
    }

    //using binding to inflate main fragment layout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        //set the recycler view adapter
        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it) })

        //Observer from when user selects an asteroid and navigate to details fragment
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        //Observer for when saved asteroid list is changed to set exists boolean in viewmodel
        viewModel.savedAsteroidsList.observe(viewLifecycleOwner, Observer{
            if (!it.isNullOrEmpty())
                viewModel.setFavouritesFound()
            else
                viewModel.resetFavouritesFound()
        })

        setHasOptionsMenu(true)


        return binding.root
    }

    //inflate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //when user selects from options menu, update viewmodel to change list of asteroids to show
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids -> viewModel.showWeekAsteroids()
            R.id.show_today_asteroids -> viewModel.showTodayAsteroids()
            R.id.show_saved_asteroids ->
                if(viewModel.existFavourites) viewModel.showSavedAsteroids()
                else
                    //show toast if there are no asteroids saved
                    makeText(context,R.string.no_saved_asteroids, LENGTH_LONG).show()
        }
        return true
    }


}

