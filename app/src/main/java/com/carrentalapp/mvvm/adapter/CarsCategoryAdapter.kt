package com.carrentalapp.mvvm.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrentalapp.mvvm.R
import com.carrentalapp.mvvm.data.model.CategoryModel
import com.carrentalapp.mvvm.databinding.CarsCategoryLayoutBinding

class CarsCategoryAdapter : RecyclerView.Adapter<CarsCategoryAdapter.ViewHolder>() {
    private var carsList = ArrayList<CategoryModel>()
    var itemClickCars : ((String) -> Unit)? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CarsCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = carsList[position]
        holder.bind(category)

        // Nếu vị trí hiện tại là vị trí đã chọn, thì thay đổi màu nền của TextView
        if (selectedPosition == position) {
            holder.binding.tvNameCategory.setBackgroundColor(Color.parseColor("#2B4C59"))
            holder.binding.tvNameCategory.setTextColor(Color.WHITE)
        } else {
            holder.binding.tvNameCategory.setBackgroundResource(R.drawable.bg_dark_salmon_outside)
            holder.binding.tvNameCategory.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            itemClickCars?.invoke(category.id)
        }
    }

    inner class ViewHolder(val binding: CarsCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {
            binding.tvNameCategory.text = category.name

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCars(cars: ArrayList<CategoryModel>) {
        this.carsList = cars
        notifyDataSetChanged()
    }
}