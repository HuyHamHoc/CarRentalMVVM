package com.carrentalapp.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.databinding.CarsFavoritesItemLayoutBinding
import com.carrentalapp.mvvm.model.Cars

open class CarsFavoritesAdapter() : RecyclerView.Adapter<CarsFavoritesAdapter.VH>() {

    private var cars: List<Cars> = emptyList()

    fun setCars(cars: List<Cars>) {
        this.cars = cars
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CarsFavoritesItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = cars.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(cars[position])
    }

    class VH(private val binding: CarsFavoritesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cars: Cars) {
            binding.run {
                txtName.text = cars.name
                txtPrice.text = "$${cars.price}"
                imgFavorite.setImageResource(R.drawable.ic_favorite_cars)

            }

            var num= 0
            binding.btnIncrease.setOnClickListener {
                num++
                binding.txtQuantity.text = num.toString()
            }

            binding.btnReduce.setOnClickListener {
                num--
                binding.txtQuantity.text = num.toString()
            }
        }
    }
}