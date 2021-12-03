package me.gmcardoso.listpad

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.CategoryDAO
import me.gmcardoso.listpad.database.dao.ListDAO
import me.gmcardoso.listpad.databinding.ActivityCreateOrEditListBinding
import me.gmcardoso.listpad.model.Category
import me.gmcardoso.listpad.model.List

class CreateOrEditListActivity : AppCompatActivity() {

    private lateinit var activityCreateOrEditListBinding: ActivityCreateOrEditListBinding
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var listDAO: ListDAO
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
            list.name = listName
            list.description = listDescription
            list.urgent = listIsUrgent
            list.categoryId = listCategory.id
            listDAO.update(list)
            finish()
        } else {
            val newList = List(null, listName, listDescription, listIsUrgent, listCategory.id)

            val listWIthSameName = listDAO.getByName(listName)

            if(listWIthSameName.size > 0) {
                AlertDialog.Builder(this)
                    .setTitle("Ação indisponível")
                    .setMessage("Não é possivel criar listas com nome repetido, favor alterar o nome da lista a ser criada.")
                    .setNeutralButton("Entendi", null)
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
        } else {
            activityCreateOrEditListBinding.ivDeleteIcon.visibility = View.GONE
        }

        if(this::list.isInitialized) {
            setListFields(list)
        }
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

    private fun setListFields(list: List) {
        val categoryIndex = categories.indexOfFirst { category -> category.id == list.categoryId }

        activityCreateOrEditListBinding.etListName.setText(list.name)
        activityCreateOrEditListBinding.etListDescription.setText(list.description)
        activityCreateOrEditListBinding.cbUrgent.isChecked = list.urgent == 1
        activityCreateOrEditListBinding.spCategories.setSelection(categoryIndex)
    }
}