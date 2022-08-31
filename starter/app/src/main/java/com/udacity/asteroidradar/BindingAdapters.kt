package com.udacity.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.main.NASANEoWsApiStatus

private val TAG = "BindingAdapters"

@BindingAdapter("errorStatus")
fun bindErrorStatus(imageView: ImageView, status: NASANEoWsApiStatus) {
    imageView.visibility = when (status) {
        NASANEoWsApiStatus.FAILURE -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("loadingStatus")
fun bindLoadingStatus(progressBar: ProgressBar, status: NASANEoWsApiStatus) {
    progressBar.visibility = when (status) {
        NASANEoWsApiStatus.LOADING -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("imageOfTheDay")
fun bindImageOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    imageView.apply {
        if (pictureOfDay != null || pictureOfDay?.mediaType == "image") {
            val imageUri = pictureOfDay.url.toUri().buildUpon().scheme("https").build()
            Picasso.with(context)
                .load(imageUri)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .into(this)
            contentDescription = pictureOfDay.title
            Log.i(TAG, "Picture Loaded Successfully from: $imageUri")
        } else {
            Picasso.with(context)
                .load(R.drawable.ic_baseline_broken_image_24)
                .into(this)
            Log.i(TAG, "Error Loading The Picture")
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    imageView.apply {
        if (isHazardous) {
            setImageResource(R.drawable.ic_status_potentially_hazardous)
            contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
        } else {
            setImageResource(R.drawable.ic_status_normal)
            contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
        }
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    imageView.apply {
        if (isHazardous) {
            setImageResource(R.drawable.asteroid_hazardous)
            contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
        } else {
            setImageResource(R.drawable.asteroid_safe)
            contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
        }
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
