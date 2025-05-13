import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epf.android_project.model.CartWithProduct
import com.epf.android_project.repository.CartRepository
import com.epf.android_project.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val cartRepository = CartRepository()

    private val _cartItems = MutableLiveData<Resource<CartWithProduct>>()
    val cartItems: LiveData<Resource<CartWithProduct>> = _cartItems

    private val _updateCartResult = MutableLiveData<Resource<Boolean>>()
    val updateCartResult: LiveData<Resource<Boolean>> = _updateCartResult

    init {
        loadCartItems()
    }

    fun loadCartItems() {
        viewModelScope.launch {
            _cartItems.value = Resource.Loading()
            cartRepository.getCartWithProductDetails().collect { result ->
                _cartItems.value = result
            }
        }
    }

    fun updateItemQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(productId, quantity).collect { result ->
                _updateCartResult.value = result
                if (result is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId).collect { result ->
                _updateCartResult.value = result
                if (result is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart().collect { result ->
                _updateCartResult.value = result
                if (result is Resource.Success) {
                    loadCartItems()
                }
            }
        }
    }

    // Calculer le montant total du panier
    fun calculateTotal(): Double {
        val cartData = _cartItems.value?.data
        return cartData?.items?.sumOf { it.product.price * it.quantity } ?: 0.0
    }
}