package com.udacity.asteroidradar

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidViewHolderBinding
import com.udacity.asteroidradar.main.MainViewModel

private val TAG = "AsteroidAdapter"

class AsteroidAdapter(private val viewModel: MainViewModel): ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(viewModel, parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AsteroidViewHolder private constructor(private val itemBinding: AsteroidViewHolderBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        companion object {
            fun from(viewModel: MainViewModel, parent: ViewGroup): AsteroidViewHolder {
                val binding =
                    AsteroidViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.viewModel = viewModel
                return AsteroidViewHolder(binding)
            }
        }

        fun bind(asteroid: Asteroid) {
            itemBinding.asteroid = asteroid
        }
    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            Log.i(TAG, "areItemsTheSame() Called")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            Log.i(TAG, "areContentsTheSame() Called")
            return oldItem == newItem
        }

    }

}