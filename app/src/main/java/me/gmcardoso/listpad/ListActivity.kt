package me.gmcardoso.listpad

import android.app.AlertDialog
import android.content.DialogInterface
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import me.gmcardoso.listpad.adapter.ItemAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.ItemDAO
import me.gmcardoso.listpad.database.dao.ListDAO
import me.gmcardoso.listpad.databinding.ActivityListBinding
import me.gmcardoso.listpad.model.Item
import java.util.*
import kotlin.collections.ArrayList

class ListActivity : AppCompatActivity() {

    private lateinit var activityListBinding: ActivityListBinding
    private lateinit var listDAO: ListDAO
    private lateinit var itemDAO: ItemDAO
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var items: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter
    private var itemHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityListBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(activityListBinding.root)

        databaseHandler = DatabaseHandler(this)
        itemDAO = ItemDAO(databaseHandler)
        listDAO = ListDAO(databaseHandler)

        val listName = this.intent.getStringExtra("listName")
        activityListBinding.tvTitle.text = listName

        updateUI()
    }

    private fun updateUI() {
        val listId = this.intent.getIntExtra("listId", 0)
        items = itemDAO.getByList(listId)
        itemAdapter = ItemAdapter(items)

        activityListBinding.rvItems.layoutManager = LinearLayoutManager(this)
        activityListBinding.rvItems.adapter = itemAdapter

        activityListBinding.ivDeleteIcon.setOnClickListener { showDeleteListDialog() }

        val listener = object: ItemAdapter.ItemListener {
            override fun onItemCheck(pos: Int) {
                completeItem(pos)
            }

            override fun onItemDelete(pos: Int) {
                deleteItem(pos)
            }

            override fun onItemDescriptionUpdated(pos: Int, description: String, instantly: Boolean) {
                val delayTime: Long = if(instantly) 0 else 1000
                itemHandler.removeCallbacksAndMessages(null)
                itemHandler.postDelayed({
                    if(pos < items.size) {
                        updateItem(pos, description)
                    } else {
                        createItem(description)
                    }
                }, delayTime)
            }
        }

        itemAdapter.setOnClickListener(listener)
    }

    private fun completeItem(pos: Int) {
        val item = items[pos]
        item.completed = if(item.completed == 1) 0 else 1
        itemDAO.update(item)
        updateUI()
    }

    private fun createItem(description: String) {

        if(description.isEmpty()) return

        val listId = this.intent.getIntExtra("listId", 0)
        val item = Item(null, description, 0, listId)
        itemDAO.save(item)
        updateUI()
    }

    private fun updateItem(pos: Int, description: String) {
        if(description == items[pos].description) {
            Log.d("AQUI", "DESCRICAO TA IGUAL ERA ANTES")
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

    private fun showDeleteListDialog() {
        AlertDialog.Builder(this)
            .setTitle("Excluir lista")
            .setMessage("Deseja realmente excluir a lista selecionada?")
            .setPositiveButton(
                "Sim"
            ) { _, _ -> deleteList() }
            .setNegativeButton("Ní", null)
            .show()
    }

    private fun deleteList() {
        val listId = this.intent.getIntExtra("listId", 0)
        listDAO.delete(listId)
        finish()


    }
}