package com.carrentalapp.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrentalapp.mvvm.databinding.CarsItemLayoutBinding
import com.carrentalapp.mvvm.model.Cars

class CarsAdapter : RecyclerView.Adapter<CarsAdapter.VH>() {

    private var cars: List<Cars> = emptyList()

    fun setCars(cars: List<Cars>){
        this.cars = cars
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CarsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(cars[position])
    }

    class VH(private val binding: CarsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cars: Cars) {
            binding.run {
                txtName.text = cars.name
                txtPrice.text = "$${cars.price}/day"
            }
        }
    }
}