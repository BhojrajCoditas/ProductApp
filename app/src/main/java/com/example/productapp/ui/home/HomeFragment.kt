package com.example.productapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.productapp.R
import com.example.productapp.adapter.ProductAdapter
import com.example.productapp.data.model.Product
import com.example.productapp.databinding.FragmentHomeBinding
import com.example.productapp.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter

    private var allProducts: List<Product> = emptyList()
    private var filteredProducts: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeProducts()
        setupSearch()
//        setupCategoryButtons()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        productAdapter = ProductAdapter(emptyList()) { product ->
            // TODO: Navigate to ProductDetailFragment later
        }
        binding.recyclerViewProducts.adapter = productAdapter
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            viewModel.productList.collectLatest { products ->
                allProducts = products
                filteredProducts = products
                productAdapter.updateList(filteredProducts)

                // Dynamically create category buttons here
                val categories = products.map { it.category }.distinct()
                setupCategoryButtons(listOf("All") + categories)
            }
        }
    }


    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val searchedProducts = filteredProducts.filter {
                    it.title.lowercase().contains(query)
                }
                productAdapter.updateList(searchedProducts)
            }
        })
    }

    private fun setupCategoryButtons(categories: List<String>) {
        val categoryContainer = binding.categoryContainer  // LinearLayout inside HorizontalScrollView
        categoryContainer.removeAllViews()

        categories.forEach { category ->
            val button = LayoutInflater.from(requireContext())
                .inflate(R.layout.dynamic_category_button, categoryContainer, false) as androidx.appcompat.widget.AppCompatButton

            button.text = category.capitalize()

            button.setOnClickListener {
                if (category.equals("All", ignoreCase = true)) {
                    filteredProducts = allProducts
                } else {
                    filteredProducts = allProducts.filter { it.category.equals(category, ignoreCase = true) }
                }
                productAdapter.updateList(filteredProducts)
            }

            categoryContainer.addView(button)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
