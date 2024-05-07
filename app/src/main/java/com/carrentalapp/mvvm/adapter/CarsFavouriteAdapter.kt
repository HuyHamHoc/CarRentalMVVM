package com.carrentalapp.mvvm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.data.model.CarsFavourite
import com.carrentalapp.mvvm.databinding.CarsFavoritesItemLayoutBinding
import kotlin.collections.ArrayList

class CarsFavouriteAdapter(private val onDeleteClick: (String) -> Unit) : RecyclerView.Adapter<CarsFavouriteAdapter.ViewHolder>() {
    private var carsList: List<CarsFavourite> = ArrayList()
    var itemClickCars : ((CarsFavourite) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CarsFavoritesItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = carsList[position]
        holder.binding.txtName.text = carsList[position].name
        holder.binding.txtPrice.text = "$${carsList[position].price}"
        Glide.with(holder.itemView).load(carsList[position].picture).into(holder.binding.picCarFavourite)

        holder.itemView.setOnClickListener {
            itemClickCars?.invoke(carsList[position] )
        }

        holder.binding.tvDeleteFavourite.setOnClickListener {
            onDeleteClick(car.carId)
        }

    }

    class ViewHolder(val binding: CarsFavoritesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCars(cars: List<CarsFavourite>) {
        this.carsList = cars
        notifyDataSetChanged()
    }
}