package com.carrentalapp.mvvm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrentalapp.mvvm.data.model.Transaction
import com.carrentalapp.mvvm.databinding.CarsMyBookLayoutBinding


class CarsMyBookAdapter : RecyclerView.Adapter<CarsMyBookAdapter.ViewHolder>() {
    private var carsList = ArrayList<Transaction>()
    var itemClickCars : ((Transaction) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CarsMyBookLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return carsList.size
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtPrice.text = "$${carsList[position].totalPrice}"
        holder.binding.txtStatus.text = carsList[position].status
        holder.binding.txtAddress.text = carsList[position].address

        holder.itemView.setOnClickListener {
            itemClickCars?.invoke(carsList[position] )
        }
    }

    class ViewHolder(val binding: CarsMyBookLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCars(cars: ArrayList<Transaction>) {
        this.carsList = cars
        notifyDataSetChanged()
    }
}