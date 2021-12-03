package me.gmcardoso.listpad.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import me.gmcardoso.listpad.model.Item

class ItemDAO(private var dbHelper: SQLiteOpenHelper) {

    companion object {
        private const val TABLE_NAME = "Items"
        private const val ID = "Id"
        private const val DESCRIPTION = "Description"
        private const val COMPLETED = "Completed"
        private const val LIST_ID = "ListId"
    }

    fun save(item: Item): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(DESCRIPTION, item.description)
        values.put(COMPLETED, item.completed)
        values.put(LIST_ID, item.listId)
        val success = db.insert(TABLE_NAME,null,values)
        db.close()
        return success
    }

    fun get(_id: Int): Item {
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        val item = Item(cursor.getInt(0),
            cursor.getString(1),
            cursor.getInt(2),
            cursor.getInt(3))
        cursor.close()
        return item
    }

    fun getByList(_listId: Int): ArrayList<Item> {
        val listOfItems = ArrayList<Item>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $LIST_ID = '$_listId'"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val item = Item(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3))
                    listOfItems.add(item)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return listOfItems
    }

    fun getByDescription(_itemDescription: String, _listId: Int): ArrayList<Item> {
        val listOfItems = ArrayList<Item>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $DESCRIPTION = '$_itemDescription' AND $LIST_ID = '$_listId'"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val item = Item(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3))
                    listOfItems.add(item)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return listOfItems
    }

    fun update(item: Item): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DESCRIPTION, item.description)
            put(COMPLETED, item.completed)
            put(LIST_ID, item.listId)
        }
        val success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(item.id.toString())).toLong()
        db.close()
        return ("$success").toInt() != -1
    }

    fun delete(_id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        return ("$success").toInt() != -1
    }

    fun deleteByList(_listId: Int): Boolean {
        val db = dbHelper.writableDatabase
        val success = db.delete(TABLE_NAME, "$LIST_ID=?", arrayOf(_listId.toString())).toLong()
        return ("$success").toInt() != -1
    }
}