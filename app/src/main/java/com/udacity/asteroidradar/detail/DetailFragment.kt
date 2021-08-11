package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MainViewModelFactory


class DetailFragment : Fragment() {

    //create viewmodel
    private val viewModel: DetailViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, DetailViewModelFactory(activity.application))
            .get(DetailViewModel::class.java)
    }

    //variable to store asteroid ID for saving
    var asteroidID: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        //get selected asteroid from detail fragment
        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        // set asteroid ID
        asteroidID = asteroid.id

        binding.asteroid = asteroid

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    //function to display help message
    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }

    //create options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //save favourite asteroid to database
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_asteroid -> viewModel.setFavToDatabase(asteroidID)
        }
        return true

    }
}
