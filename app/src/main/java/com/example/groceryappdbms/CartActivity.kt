package com.example.groceryappdbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

class CartActivity : AppCompatActivity() {

    private lateinit var dbHandler: dbHandler
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val splashScreen = findViewById<CardView>(R.id.splash_screen)
        var rootLayout = findViewById<LinearLayout>(R.id.parent_layout)



        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
      //  val textView = findViewById<TextView>(R.id.text_view)
        val placeOrder = findViewById<Button>(R.id.placeorder)

        placeOrder.setOnClickListener {
            animationView.loop(true)
            splashScreen.visibility = View.VISIBLE
        }
        rootLayout.setOnClickListener {
            splashScreen.visibility = View.GONE
        }


        dbHandler = dbHandler(this) // Create instance of dbHandler for cart_table

     //   getItems()
        // Retrieve the list of cart items from the database
        val cartItems = dbHandler.getAllCartItems()


        val total = findViewById<TextView>(R.id.TotalCost)

        // Create a new RecyclerView and set its adapter
        cartRecyclerView = findViewById(R.id.cartrecyclerview)
        cartAdapter = CartAdapter(cartItems,dbHandler,this,total)
        cartLayoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = cartLayoutManager

        TotalCost()

    }

    private fun TotalCost() {
        var totalCost = 0.00

        val total = findViewById<TextView>(R.id.TotalCost)
        val cartItems = dbHandler.getAllCartItems()
        for(item in cartItems){
            val itemCost = item.quantity * item.price
            totalCost += itemCost
        }
        total.text = "₹${String.format("%.2f", totalCost)}"
    }

//    private fun getItems() {
//        val itemId = intent.getIntExtra("item_id", 0)
//        val itemName = intent.getStringExtra("item_name").toString()
//        val itemPrice = intent.getIntExtra("item_price", 0)
//        val itemQuantity = intent.getIntExtra("item_quantity", 0)
//
//        // Add the item to the cart_table
//        dbHandler.addCartItem(GroceryItem(itemId, itemName, itemPrice, itemQuantity))
//
//    }


}