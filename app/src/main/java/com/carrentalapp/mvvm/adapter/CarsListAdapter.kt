package com.carrentalapp.mvvm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carrentalapp.mvvm.data.model.CarsList
import com.carrentalapp.mvvm.databinding.CarsItemLayoutBinding

class CarsListAdapter : RecyclerView.Adapter<CarsListAdapter.ViewHolder>() {
    private var carsList = ArrayList<CarsList>()
    var itemClickCars : ((CarsList) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CarsItemLayoutBinding.inflate(
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
        holder.binding.txtName.text = carsList[position].name
        holder.binding.txtPrice.text = "$${carsList[position].price}"
        Glide.with(holder.itemView).load(carsList[position].picture).into(holder.binding.imgCar)

        holder.itemView.setOnClickListener {
            itemClickCars!!.invoke(carsList[position] )
        }
    }

    class ViewHolder(val binding: CarsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCars(cars: ArrayList<CarsList>) {
        this.carsList = cars
        notifyDataSetChanged()
    }
}