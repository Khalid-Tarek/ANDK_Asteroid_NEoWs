package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.detail.DetailFragment
import com.udacity.asteroidradar.detail.DetailFragmentArgs

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application

        val dataSource = AsteroidDatabase.getInstance(application).asteriodDao

        val mainViewModelFactory = MainViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
