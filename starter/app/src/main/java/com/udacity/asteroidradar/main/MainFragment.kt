package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

//        //Testing purposes only
//        lifecycleScope.launch {
//            AsteroidDatabase.getInstance(requireContext()).asteriodDao.clear()
//        }

        initializeViewModel()

        initializeAdapter()

        setupObservers()

        return binding.root
    }

    private fun initializeViewModel() {
        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application)
        val mainViewModelFactory = MainViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun initializeAdapter() {
        adapter = AsteroidAdapter(viewModel)
        binding.asteroidRecycler.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isEmpty())
                    viewModel.loadAsteroids()
                adapter.submitList(it)
            }
        })
        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneNavigating()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
