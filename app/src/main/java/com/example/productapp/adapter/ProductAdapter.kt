package com.example.productapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productapp.R
import com.example.productapp.data.model.Product

class ProductAdapter(
    private var productList: List<Product>, // Make it mutable (var)
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        val textBrand: TextView = itemView.findViewById(R.id.textBrand)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.textTitle.text = product.title
        holder.textBrand.text = product.brand.replaceFirstChar { it.uppercase() }
        holder.textPrice.text = "₹${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.imageProduct)

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = productList.size

    // Add this function to update the list dynamically
    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
