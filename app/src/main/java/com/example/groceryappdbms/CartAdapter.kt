package com.example.groceryappdbms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private var itemList: List<GroceryItem>, private val dbHandler: dbHandler, private val context: Context, private val totalCostTextView: TextView) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
        holder.itemPrice.text = "₹${currentItem.price}"
        holder.itemQuantity.text = currentItem.quantity.toString()
        // Update the total cost when the quantity is changed
        holder.minusButton.setOnClickListener {
            if (currentItem.quantity > 1) {
                currentItem.quantity -= 1
                dbHandler.updateCartItem(currentItem)
                holder.itemQuantity.text = currentItem.quantity.toString()
                calculateTotalCost()
            }
        }

        holder.plusButton.setOnClickListener {
            if(currentItem.quantity <= 50) {
                currentItem.quantity += 1
                dbHandler.updateCartItem(currentItem)
                holder.itemQuantity.text = currentItem.quantity.toString()
                calculateTotalCost()
            }
        }

        holder.deleteButton.setOnClickListener {
            dbHandler.deleteCartItem(currentItem)
            val mutableItemList = itemList.toMutableList()
            mutableItemList.remove(currentItem)
            itemList = mutableItemList.toList()
            notifyDataSetChanged()
            calculateTotalCost()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Function to calculate the total cost
    private fun calculateTotalCost() {
       var totalCost = itemList.sumOf { it.price * it.quantity } // Calculate the total cost
        totalCostTextView.text = "₹${String.format("%.2f", totalCost)}" // Display the total cost in the TextView
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.nameTextView)
        val itemPrice: TextView = itemView.findViewById(R.id.priceTextView)
        val itemQuantity: TextView = itemView.findViewById(R.id.quantityTextView)
        val minusButton: ImageButton = itemView.findViewById(R.id.minusButton)
        val plusButton: ImageButton = itemView.findViewById(R.id.plusButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }
}
