package com.epf.android_project.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epf.android_project.R
import com.epf.android_project.databinding.ItemProductBinding
import com.epf.android_project.model.Product
import com.epf.android_project.utils.FavorisManager

class ProductAdapter(
    var onItemClick: ((Product) -> Unit)? = null
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    var onFavoriteClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productTitle.text = product.title
            binding.productPrice.text = "€${product.price}"
            Glide.with(binding.productImage.context)
                .load(product.image)
                .centerCrop()
                .into(binding.productImage)

            val context = binding.root.context
            val isFav = FavorisManager.isFavorite(context, product.id)
            val heartIcon = if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            binding.favoriteIcon.setImageResource(heartIcon)

            binding.favoriteIcon.setOnClickListener {
                FavorisManager.toggleFavorite(context, product.id)
                onFavoriteClick?.invoke(product) // laisse l’adapter gérer la nouvelle liste
            }


            binding.root.setOnClickListener {
                onItemClick?.invoke(product)
            }


        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }
}

