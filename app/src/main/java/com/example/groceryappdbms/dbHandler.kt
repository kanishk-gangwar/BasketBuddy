package com.example.groceryappdbms

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*


class dbHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "grocery_database"
        private const val TABLE_NAME = "grocery_items"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PRICE = "price"
        private const val KEY_QUANTITY = "quantity"
        private const val CART_TABLE_NAME = "cart_items"
        private const val CART_KEY_ID = "id"
        private const val CART_KEY_NAME = "name"
        private const val CART_KEY_PRICE = "price"
        private const val CART_KEY_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_PRICE INTEGER, $KEY_QUANTITY INTEGER)"
        db?.execSQL(createTable)
        val createCartTable = "CREATE TABLE $CART_TABLE_NAME ($CART_KEY_ID INTEGER PRIMARY KEY, $CART_KEY_NAME TEXT, $CART_KEY_PRICE INTEGER, $CART_KEY_QUANTITY INTEGER)"
        db?.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addGroceryItem(item: GroceryItem) {
        val id = Random().nextInt(1000000)
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID, id)
        values.put(KEY_NAME, item.name)
        values.put(KEY_PRICE, item.price)
        values.put(KEY_QUANTITY, item.quantity)
        var result =  db.insert(TABLE_NAME, null, values)
        if(result == -1.toLong())
           Log.d("Result","Fail")
        else{
            Log.d("Result","Success")
        }


        db.close()
    }

    fun updateGroceryItem(item: GroceryItem): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, item.name)
        values.put(KEY_PRICE, item.price)
        values.put(KEY_QUANTITY, item.quantity)
        val update = db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(item.id.toString()))
        db.close()
        return update
    }

    fun deleteGroceryItem(item: GroceryItem) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(item.id.toString()))
        db.close()
    }

    fun addCartItem(item: GroceryItem) {
        val db = this.writableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME WHERE $CART_KEY_NAME=? AND $CART_KEY_PRICE=?"
        val cursor = db.rawQuery(query, arrayOf(item.name, item.price.toString()))

        if (cursor.count == 0) {
            // Item not already in cart, so add it with max quantity
            val maxQuantityQuery = "SELECT $KEY_QUANTITY FROM $TABLE_NAME WHERE $KEY_NAME=? AND $KEY_PRICE=?"
            val maxQuantityCursor = db.rawQuery(maxQuantityQuery, arrayOf(item.name, item.price.toString()))

            if (maxQuantityCursor.moveToFirst()) {


                item.maxQuantity = maxQuantityCursor.getInt(maxQuantityCursor.getColumnIndex(KEY_QUANTITY))
                val values = ContentValues()
                values.put(CART_KEY_NAME, item.name)
                values.put(CART_KEY_PRICE, item.price)
                values.put(CART_KEY_QUANTITY, 1)
                db.insert(CART_TABLE_NAME, null, values)
            }

            maxQuantityCursor.close()
        }

        cursor.close()
        db.close()
    }


    fun updateCartItem(item: GroceryItem): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, item.name)
        values.put(KEY_PRICE, item.price)
        values.put(KEY_QUANTITY, item.quantity)
        val update = db.update(CART_TABLE_NAME, values, "$KEY_ID=?", arrayOf(item.id.toString()))
        db.close()
        return update
    }
    fun deleteCartItem(item: GroceryItem) {
        val db = this.writableDatabase
        db.delete(CART_TABLE_NAME, "$CART_KEY_ID = ?",arrayOf(item.id.toString()))
        db.close()
    }


    fun getAllCartItems(): List<GroceryItem> {
        val itemList = mutableListOf<GroceryItem>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                val quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
                itemList.add(GroceryItem(id, name, price, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }


    fun getAllGroceryItems(): List<GroceryItem> {
        val itemList = mutableListOf<GroceryItem>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                val quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
                itemList.add(GroceryItem(id, name, price, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }
    fun updateData() {
        val db = this.writableDatabase
        val query = "Select * from " + CART_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(CART_KEY_QUANTITY,(result.getInt(result.getColumnIndex(CART_KEY_QUANTITY))+1))
                db.update(TABLE_NAME,cv, "$CART_KEY_ID=? AND $CART_KEY_NAME=?",
                    arrayOf(result.getString(result.getColumnIndex(CART_KEY_ID)),
                        result.getString(result.getColumnIndex(CART_KEY_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }

    fun updateCartItem2(item: GroceryItem) {
        val db = this.writableDatabase

        // Get the max quantity for the item
        val maxQuantityQuery = "SELECT $KEY_QUANTITY FROM $TABLE_NAME"
        val maxQuantityCursor = db.rawQuery(maxQuantityQuery, arrayOf(item.name, item.price.toString()))
        var maxQuantity = 0

        if (maxQuantityCursor.moveToFirst()) {
            maxQuantity = maxQuantityCursor.getInt(maxQuantityCursor.getColumnIndex(KEY_QUANTITY))
        }

        maxQuantityCursor.close()

        // Update the cart item quantity
        val newQuantity = if (item.quantity > maxQuantity) maxQuantity else item.quantity
        val updateQuery = "UPDATE $CART_TABLE_NAME SET $CART_KEY_QUANTITY=? WHERE $CART_KEY_NAME=? AND $CART_KEY_PRICE=?"
        db.execSQL(updateQuery, arrayOf(newQuantity, item.name, item.price.toString()))

        db.close()
    }

}