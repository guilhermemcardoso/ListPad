package me.gmcardoso.listpad

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import me.gmcardoso.listpad.adapter.ItemAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.ItemDAO
import me.gmcardoso.listpad.database.dao.ListDAO
import me.gmcardoso.listpad.databinding.ActivityListBinding
import me.gmcardoso.listpad.model.Item
import kotlin.collections.ArrayList

class ListActivity : AppCompatActivity() {

    private lateinit var activityListBinding: ActivityListBinding
    private lateinit var listDAO: ListDAO
    private lateinit var itemDAO: ItemDAO
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var items: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityListBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(activityListBinding.root)

        databaseHandler = DatabaseHandler(this)
        itemDAO = ItemDAO(databaseHandler)
        listDAO = ListDAO(databaseHandler)

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val listId = this.intent.getIntExtra("listId", 0)

        val list = listDAO.get(listId)
        Log.d("TESTE", if(list == null) "LIST IS NULL" else list.toString())
        if(list == null) finish()

        if(list != null) {
            activityListBinding.tvTitle.text = list!!.name

            items = itemDAO.getByList(listId)
            itemAdapter = ItemAdapter(items)

            activityListBinding.rvItems.layoutManager = LinearLayoutManager(this)
            activityListBinding.rvItems.adapter = itemAdapter

            activityListBinding.ivEditIcon.setOnClickListener { showUpdateScreen() }

            val listener = object: ItemAdapter.ItemListener {

                override fun onItemCheck(pos: Int) {
                    completeItem(pos)
                }

                override fun onItemDelete(pos: Int) {
                    deleteItem(pos)
                }

                override fun onItemCreateOrUpdate(pos: Int, description: String) {

                    if(pos < items.size) {
                        updateItem(pos, description)
                    } else {
                        createItem(description)
                    }
                }
            }

            itemAdapter.setOnClickListener(listener)
        }
    }

    private fun showUpdateScreen() {
        val listId = this.intent.getIntExtra("listId", 0)
        val intent = Intent(this, CreateOrEditListActivity::class.java)
        intent.putExtra("listId", listId)
        startActivity(intent)
    }

    private fun completeItem(pos: Int) {
        val item = items[pos]
        item.completed = if(item.completed == 1) 0 else 1
        itemDAO.update(item)
        updateUI()
    }

    private fun createItem(description: String) {
        val listId = this.intent.getIntExtra("listId", 0)
        if(description.isEmpty()) return

        val existingItems = itemDAO.getByDescription(description, listId)

        if(existingItems.size > 0) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.unavailable_action))
                .setMessage(getString(R.string.items_with_same_description_message))
                .setNeutralButton(getString(R.string.understood), null)
                .show()
            return
        }

        val item = Item(null, description, 0, listId)
        itemDAO.save(item)
        updateUI()
    }

    private fun updateItem(pos: Int, description: String) {
        val listId = this.intent.getIntExtra("listId", 0)
        if(description == items[pos].description) {
            return
        }

        val existingItems = itemDAO.getByDescription(description, listId)

        if(existingItems.size > 0) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.unavailable_action))
                .setMessage(getString(R.string.items_with_same_description_message))
                .setNeutralButton(getString(R.string.understood), null)
                .show()
            return
        }

        val item = items[pos]
        item.description = description
        itemDAO.update(item)
        updateUI()
    }

    private fun deleteItem(pos: Int) {
        items[pos].id?.let { itemDAO.delete(it) }
        updateUI()
    }
}