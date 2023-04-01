package com.example.groceryappdbms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Additems : AppCompatActivity() {

    private lateinit var dbHandler: dbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additems)

        dbHandler = dbHandler(this)

        findViewById<Button>(R.id.button_save).setOnClickListener {
            var name = findViewById<EditText>(R.id.edit_text_name).text.toString()
            val priceText = findViewById<EditText>(R.id.edit_text_price).text.toString()
            val quantityText = findViewById<EditText>(R.id.edit_text_quantity).text.toString()

            // Check if price and quantity are valid numbers
            val price = priceText.toDoubleOrNull()
            val quantity = quantityText.toIntOrNull()

            if (price != null && quantity != null) {
                val groceryItem = GroceryItem(name = name, price = price, quantity = quantity)
                dbHandler.addGroceryItem(groceryItem)

                findViewById<EditText>(R.id.edit_text_name).text!!.clear()
                findViewById<EditText>(R.id.edit_text_price).text!!.clear()
                findViewById<EditText>(R.id.edit_text_quantity).text!!.clear()

                Toast.makeText(this, "Item added to the grocery list!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid price and quantity!", Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<Button>(R.id.quit).setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
    override fun onDestroy() {
        dbHandler.close()
        super.onDestroy()
    }
}