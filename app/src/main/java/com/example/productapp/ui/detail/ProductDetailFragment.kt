package com.example.productapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.productapp.R
import com.example.productapp.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    private var portionCount = 1
    private var unitPrice = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product
        unitPrice = product.price

        // this and requiredContext()
        // Load Image Correctly
        Glide.with(this)
            .load(product.image)
            .into(binding.imageProductBanner)


        // Set UI Data
        binding.textProductTitle.text = product.title
        binding.textDescription.text = product.description
        binding.textRating.text = "4.9"
        binding.textDuration.text = "• 26 mins"

        updateTotalPrice()

        setupPortionButtons()
        setupSpicySeekBar()

        // Back Button
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupPortionButtons() {
        binding.buttonIncrease.setOnClickListener {
            portionCount++
            binding.textPortionCount.text = portionCount.toString()
            updateTotalPrice()
        }

        binding.buttonDecrease.setOnClickListener {
            if (portionCount > 1) {
                portionCount--
                binding.textPortionCount.text = portionCount.toString()
                updateTotalPrice()
            }
        }    
    }
     // explore string extension
    private fun updateTotalPrice() {
        val totalPrice = unitPrice * portionCount
        binding.textTotalPrice.text = "₹%.2f".format(totalPrice)
    }

    private fun setupSpicySeekBar() {
        binding.seekBarSpicy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Optional: Update spicy level label if you wish
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
