package com.epf.android_project.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.epf.android_project.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(
            onRemoveClick = { cartViewModel.removeProductFromCart(it) },
            onDecreaseClick = { cartViewModel.decreaseProductQuantity(it) }
        )

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        lifecycleScope.launch {
            cartViewModel.cartItems.collectLatest { items ->
                cartAdapter.submitList(items)
                binding.totalPriceText.text = "Total: €${String.format("%.2f", cartViewModel.getTotalPrice())}"
            }
        }

        binding.checkoutButton.setOnClickListener {
            // Action de validation du panier (ex: Toast ou navigation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
