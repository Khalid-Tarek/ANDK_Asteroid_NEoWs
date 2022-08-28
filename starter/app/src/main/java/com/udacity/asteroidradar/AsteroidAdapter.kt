package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidViewHolderBinding
import com.udacity.asteroidradar.main.MainViewModel

class AsteroidAdapter(private val viewModel: MainViewModel): RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {
    var myData = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val binding = AsteroidViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(myData[position])
    }

    override fun getItemCount() = myData.size

    class AsteroidViewHolder(private val itemBinding: AsteroidViewHolderBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(asteroid: Asteroid) {
            itemBinding.asteroid = asteroid
        }
    }
}