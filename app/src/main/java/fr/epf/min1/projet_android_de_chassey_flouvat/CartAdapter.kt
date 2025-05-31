package fr.epf.min1.projet_android_de_chassey_flouvat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.min1.projet_android_de_chassey_flouvat.data.CartItem
import fr.epf.min1.projet_android_de_chassey_flouvat.data.CartManager

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val decreaseButton: Button = itemView.findViewById(R.id.decreaseButton)
        val increaseButton: Button = itemView.findViewById(R.id.increaseButton)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.product

        // Afficher les données du produit
        holder.productTitle.text = product.title
        holder.productPrice.text = "€${product.price}"
        holder.quantityText.text = cartItem.quantity.toString()
        holder.totalPrice.text = "€${"%.2f".format(cartItem.totalPrice)}"

        // Charger l'image
        Glide.with(holder.itemView.context)
            .load(product.image)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.productImage)

        // Bouton diminuer quantité
        holder.decreaseButton.setOnClickListener {
            val newQuantity = cartItem.quantity - 1
            if (newQuantity > 0) {
                CartManager.updateQuantity(product.id, newQuantity)
                cartItem.quantity = newQuantity
                notifyItemChanged(position)
            } else {
                CartManager.removeProduct(product.id)
                cartItems.removeAt(position)
                notifyItemRemoved(position)
            }
            onCartUpdated()
        }

        // Bouton augmenter quantité
        holder.increaseButton.setOnClickListener {
            val newQuantity = cartItem.quantity + 1
            CartManager.updateQuantity(product.id, newQuantity)
            cartItem.quantity = newQuantity
            notifyItemChanged(position)
            onCartUpdated()
        }

        // Bouton supprimer complètement
        holder.removeButton.setOnClickListener {
            CartManager.removeProduct(product.id)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            onCartUpdated()
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newCartItems: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }
}