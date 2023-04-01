package com.example.groceryappdbms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryappdbms.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var db: dbHandler
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroceryAdapter
    private lateinit var groceryList: MutableList<GroceryItem>
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = dbHandler(this)

        setUpNavigationview()

        recyclerView = findViewById(R.id.groceryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        groceryList = db.getAllGroceryItems().toMutableList()

        adapter = GroceryAdapter(groceryList, db,this)
        recyclerView.adapter = adapter



    }

    private fun setUpNavigationview() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Cart -> { val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent) }

                R.id.add_grocery -> {
                    val intent = Intent(this, Additems::class.java)
                    startActivity(intent)
                }

                R.id.removeitems -> {
                    showBottomSheetDialog()
                }
            }

            true
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.slider_layout, null)
        bottomSheetDialog.setContentView(view)

        val button = view.findViewById<Button>(R.id.button_delete_item)
        val editText = view.findViewById<EditText>(R.id.id_item)

        button.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                val itemId = text.toIntOrNull()
                if (itemId != null) {
                    val dbHandler = dbHandler(this)
                    val groceryitem =  GroceryItem(id = itemId)
                    dbHandler.deleteGroceryItem(groceryitem)
                    Toast.makeText(this, "Item deleted from the grocery list!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter a valid item ID!", Toast.LENGTH_SHORT).show()
                }
            }
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}