package com.epf.android_project.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epf.android_project.R
import com.epf.android_project.model.CartItem

class CartAdapter(
    private val onRemoveClick: (product: com.epf.android_project.model.Product) -> Unit,
    private val onDecreaseClick: (product: com.epf.android_project.model.Product) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleText)
        val details: TextView = itemView.findViewById(R.id.quantityText)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
        val decreaseButton: Button = itemView.findViewById(R.id.decreaseButton)

        fun bind(item: CartItem) {
            title.text = item.product.title
            details.text = "Quantité : ${item.quantity}"

            removeButton.setOnClickListener {
                onRemoveClick(item.product)
            }

            decreaseButton.setOnClickListener {
                onDecreaseClick(item.product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = getItem(position)

        holder.title.text = item.product.title

        val unitPrice = item.product.price
        val quantity = item.quantity
        val total = unitPrice * quantity

        holder.details.text = "€${String.format("%.2f", unitPrice)} × $quantity = €${String.format("%.2f", total)}"

        holder.removeButton.setOnClickListener {
            onRemoveClick(item.product)
        }

        holder.decreaseButton.setOnClickListener {
            onDecreaseClick(item.product)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
