package me.gmcardoso.listpad

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.CategoryDAO
import me.gmcardoso.listpad.database.dao.ItemDAO
import me.gmcardoso.listpad.database.dao.ListDAO
import me.gmcardoso.listpad.databinding.ActivityCreateOrEditListBinding
import me.gmcardoso.listpad.model.Category
import me.gmcardoso.listpad.model.List

class CreateOrEditListActivity : AppCompatActivity() {

    private lateinit var activityCreateOrEditListBinding: ActivityCreateOrEditListBinding
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var listDAO: ListDAO
    private lateinit var itemDAO: ItemDAO
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var categories: ArrayList<Category>
    private lateinit var list: List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateOrEditListBinding = ActivityCreateOrEditListBinding.inflate(layoutInflater)
        setContentView(activityCreateOrEditListBinding.root)

        databaseHandler = DatabaseHandler(this)
        categoryDAO = CategoryDAO(databaseHandler)
        listDAO = ListDAO(databaseHandler)
        itemDAO = ItemDAO(databaseHandler)

        activityCreateOrEditListBinding.btnCancel.setOnClickListener {
            cancel()
        }

        activityCreateOrEditListBinding.btnSave.setOnClickListener {
            save()
        }

        updateUI()
    }

    private fun cancel() {
        finish()
    }

    private fun save() {
        val listName = activityCreateOrEditListBinding.etListName.text.toString()
        val listDescription = activityCreateOrEditListBinding.etListDescription.text.toString()
        val listIsUrgent = if (activityCreateOrEditListBinding.cbUrgent.isChecked) 1 else 0
        val listCategory = categories[activityCreateOrEditListBinding.spCategories.selectedItemPosition]

        if(this::list.isInitialized) {

            val listWIthSameName = listDAO.getByName(listName)
            val listWithSameNameAndDifferentId = listWIthSameName.find { it.id != list.id }
            if(listWithSameNameAndDifferentId != null) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.unavailable_action))
                    .setMessage(getString(R.string.lists_with_same_name_message))
                    .setNeutralButton(getString(R.string.understood), null)
                    .show()
                activityCreateOrEditListBinding.etListName.requestFocus()
            } else {
                list.name = listName
                list.description = listDescription
                list.urgent = listIsUrgent
                list.categoryId = listCategory.id
                listDAO.update(list)
                finish()
            }

        } else {
            val newList = List(null, listName, listDescription, listIsUrgent, listCategory.id)

            val listWIthSameName = listDAO.getByName(listName)

            if(listWIthSameName.size > 0) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.unavailable_action))
                    .setMessage(getString(R.string.lists_with_same_name_message))
                    .setNeutralButton(getString(R.string.understood), null)
                    .show()
                activityCreateOrEditListBinding.etListName.requestFocus()
            } else {
                listDAO.save(newList)
                finish()
            }

        }
    }

    private fun updateUI() {
        val listId = this.intent.getIntExtra("listId", 0)
        categories = categoryDAO.categories()

        var categoryAdapter: ArrayAdapter<Category> = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityCreateOrEditListBinding.spCategories.adapter = categoryAdapter
        activityCreateOrEditListBinding.ivDeleteIcon.setOnClickListener { showDeleteListDialog() }

        if(listId != 0) {
            list = listDAO.get(listId)!!
            activityCreateOrEditListBinding.tvTitle.text = resources.getString(R.string.edit_list)
        } else {
            activityCreateOrEditListBinding.ivDeleteIcon.visibility = View.GONE
        }

        if(this::list.isInitialized) {
            setListFields(list)
        }
    }

    private fun showDeleteListDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_list))
            .setMessage(getString(R.string.delete_list_confirmation_message))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ -> deleteList() }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteList() {
        val listId = this.intent.getIntExtra("listId", 0)
        listDAO.delete(listId)
        itemDAO.deleteByList(listId)
        finish()
    }

    private fun setListFields(list: List) {
        val categoryIndex = categories.indexOfFirst { category -> category.id == list.categoryId }

        activityCreateOrEditListBinding.etListName.setText(list.name)
        activityCreateOrEditListBinding.etListDescription.setText(list.description)
        activityCreateOrEditListBinding.cbUrgent.isChecked = list.urgent == 1
        activityCreateOrEditListBinding.spCategories.setSelection(categoryIndex)
    }
}