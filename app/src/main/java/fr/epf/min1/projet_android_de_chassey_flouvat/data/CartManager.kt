package fr.epf.min1.projet_android_de_chassey_flouvat.data

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addProduct(product: Product, quantity: Int = 1) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            cartItems.add(CartItem(product, quantity))
        }
    }

    fun removeProduct(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeProduct(productId)
        } else {
            cartItems.find { it.product.id == productId }?.quantity = newQuantity

        }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()
    fun getTotalPrice(): Double = cartItems.sumOf { it.totalPrice }
    fun getItemCount(): Int = cartItems.sumOf { it.quantity }
    fun clearCart() = cartItems.clear()
}