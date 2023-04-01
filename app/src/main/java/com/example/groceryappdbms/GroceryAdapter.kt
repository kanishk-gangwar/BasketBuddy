package com.example.groceryappdbms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class GroceryAdapter(private val groceryList: List<GroceryItem>, private val dbHandler: dbHandler,private val context: Context) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

    inner class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val ItemId : TextView = itemView.findViewById(R.id.itemID)
        val addButton: Button = itemView.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_item, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = groceryList[position]
        holder.nameTextView.text = item.name
        holder.priceTextView.text = "₹${item.price}"
        holder.quantityTextView.text = item.quantity.toString()
        holder.ItemId.text = item.id.toString()

        holder.addButton.setOnClickListener {
            val dbHandler = dbHandler(holder.addButton.context)
            dbHandler.addCartItem(GroceryItem(item.id, item.name, item.price, item.quantity))
            dbHandler.close()
            Toast.makeText(context,"Added to Cart Successfully!!",Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int {
        return groceryList.size
    }
}
