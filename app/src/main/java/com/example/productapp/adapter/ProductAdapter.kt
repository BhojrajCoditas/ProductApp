package com.example.productapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.productapp.R
import com.example.productapp.data.model.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onFavoriteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        val textBrand: TextView = itemView.findViewById(R.id.textBrand)
        val imageFavorite: ImageView = itemView.findViewById(R.id.imageFavorite) // Add this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.textTitle.text = product.title
        holder.textBrand.text = product.brand
        holder.textPrice.text = "₹${product.price}"

        val progressDrawable = CircularProgressDrawable(holder.itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        Glide.with(holder.itemView.context)
            .load(product.image)
            .placeholder(progressDrawable)
            .into(holder.imageProduct)


        val favoriteIcon = if (product.isFavorite) R.drawable.heart_red else R.drawable.favorite
        holder.imageFavorite.setImageResource(favoriteIcon)

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

        holder.imageFavorite.setOnClickListener {
            onFavoriteClick(product)
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
