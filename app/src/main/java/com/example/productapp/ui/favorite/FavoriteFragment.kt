package com.example.productapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.productapp.adapter.ProductAdapter
import com.example.productapp.data.model.Product
import com.example.productapp.databinding.FragmentFavoriteBinding
import com.example.productapp.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by activityViewModels()

    private lateinit var favoriteAdapter: ProductAdapter

    private var allProducts: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeAllProducts()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorites.layoutManager = GridLayoutManager(requireContext(), 2)

        favoriteAdapter = ProductAdapter(
            emptyList(),
            onItemClick = { product ->
                // Optional: Navigate to Product Detail from Favorites
            },
            onFavoriteClick = { product ->
                viewModel.removeFromFavorites(product)
            }
        )

        binding.recyclerViewFavorites.adapter = favoriteAdapter
    }

    private fun observeAllProducts() {
        // Collect the product list to get full product details
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productList.collectLatest { products ->
                allProducts = products
            }
        }
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteProducts.collectLatest { favoriteEntities ->
                if (allProducts.isEmpty()) {
                    // Product list not yet loaded, skip update
                    return@collectLatest
                }

                val favoriteIds = favoriteEntities.map { it.id }.toSet()

                val favoriteProducts = allProducts.filter { favoriteIds.contains(it.id) }
                    .map { it.copy(isFavorite = true) }

                if (favoriteProducts.isEmpty()) {
                    binding.textEmptyFavorites.visibility = View.VISIBLE
                    binding.recyclerViewFavorites.visibility = View.GONE
                } else {
                    binding.textEmptyFavorites.visibility = View.GONE
                    binding.recyclerViewFavorites.visibility = View.VISIBLE
                    favoriteAdapter.updateList(favoriteProducts)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
