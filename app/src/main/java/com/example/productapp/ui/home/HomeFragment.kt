package com.example.productapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.productapp.R
import com.example.productapp.adapter.ProductAdapter
import com.example.productapp.data.model.Product
import com.example.productapp.databinding.FragmentHomeBinding
import com.example.productapp.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by activityViewModels()

    private lateinit var productAdapter: ProductAdapter

    private var allProducts: List<Product> = emptyList()
    private var filteredProducts: List<Product> = emptyList()
    private var selectedCategory: String = "All"
    private var maxPrice: Int = 5000
    private var sortBy: String = ""
    private var showFavoritesOnly: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFilter.setOnClickListener {
            showFilterBottomSheet()
        }

        setupRecyclerView()
        observeProducts()
        observeFavorites()
        setupSearch()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        productAdapter = ProductAdapter(
            emptyList(),
            onItemClick = { product ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
                findNavController().navigate(action)
            },
            onFavoriteClick = { product ->
                if (product.isFavorite) {
                    viewModel.removeFromFavorites(product)
                } else {
                    viewModel.addToFavorites(product)
                }
            }
        )

        binding.recyclerViewProducts.adapter = productAdapter
    }

    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productList.collectLatest { products ->
                allProducts = products
                applyFilters()

                val categories = products.mapNotNull { it.category }.distinct()
                setupCategoryButtons(listOf("All") + categories)
            }
        }
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteProducts.collectLatest { favorites ->
                val favoriteIds = favorites.map { it.id }.toSet()

                val updatedList = allProducts.map { product ->
                    product.copy(isFavorite = favoriteIds.contains(product.id))
                }

                allProducts = updatedList
                applyFilters()
            }
        }
    }

    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
        })
    }

    private fun setupCategoryButtons(categories: List<String>) {
        val categoryContainer = binding.categoryContainer
        categoryContainer.removeAllViews()

        categories.forEach { category ->
            val button = LayoutInflater.from(requireContext())
                .inflate(R.layout.dynamic_category_button, categoryContainer, false) as AppCompatButton

            button.text = category.replaceFirstChar { it.uppercase() }

            button.setOnClickListener {
                selectedCategory = category
                applyFilters()
            }

            categoryContainer.addView(button)
        }
    }

    private fun showFilterBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogView)

        val seekBar = dialogView.findViewById<SeekBar>(R.id.priceSeekBar)
        val priceText = dialogView.findViewById<TextView>(R.id.textPriceRange)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupSort)
        val favoritesOnlyCheckbox = dialogView.findViewById<CheckBox>(R.id.checkFavoritesOnly)

        seekBar.progress = maxPrice
        priceText.text = "Up to ₹$maxPrice"
        favoritesOnlyCheckbox.isChecked = showFavoritesOnly

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceText.text = "Up to ₹$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        dialogView.findViewById<Button>(R.id.buttonApplyFilters).setOnClickListener {
            maxPrice = seekBar.progress
            showFavoritesOnly = favoritesOnlyCheckbox.isChecked

            sortBy = when (radioGroup.checkedRadioButtonId) {
                R.id.radioLowToHigh -> "lowToHigh"
                R.id.radioHighToLow -> "highToLow"
                R.id.radioAZ -> "az"
                R.id.radioZA -> "za"
                else -> ""
            }

            applyFilters()
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonResetFilters).setOnClickListener {
            maxPrice = 5000
            sortBy = ""
            showFavoritesOnly = false
            binding.editTextSearch.text.clear()
            selectedCategory = "All"
            applyFilters()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun applyFilters() {
        val query = binding.editTextSearch.text.toString().trim().lowercase()

        var filtered = allProducts

        // Filter by category
        if (!selectedCategory.equals("All", ignoreCase = true)) {
            filtered = filtered.filter {
                it.category?.equals(selectedCategory, ignoreCase = true) == true
            }
        }

        // Filter by search query
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.lowercase().contains(query)
            }
        }

        // Filter by price
        filtered = filtered.filter {
            it.price <= maxPrice
        }

        // Filter by favorites only (Fixed)
        if (showFavoritesOnly) {
            filtered = filtered.filter { it.isFavorite }
        }

        // Sort
        filtered = when (sortBy) {
            "lowToHigh" -> filtered.sortedBy { it.price }
            "highToLow" -> filtered.sortedByDescending { it.price }
            "az" -> filtered.sortedBy { it.title }
            "za" -> filtered.sortedByDescending { it.title }
            else -> filtered
        }

        filteredProducts = filtered
        productAdapter.updateList(filteredProducts)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
