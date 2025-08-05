package com.example.productapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.data.model.Product
import com.example.productapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> get() = _product

    fun getProductById(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductById(id)
                if (response.isSuccessful) {
                    _product.postValue(response.body())
                } else {
                    _product.postValue(null)
                }
            } catch (e: Exception) {
                _product.postValue(null)
            }
        }
    }
}
