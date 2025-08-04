package com.example.productapp.data.model

import android.media.Image
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: Int,
    val title: String,
    val image: String,
    val price: Double,
    val description: String,
    val category: String,
    val brand: String,
    val modal: String,
    val color: String,
    val discount: Int

) : Parcelable