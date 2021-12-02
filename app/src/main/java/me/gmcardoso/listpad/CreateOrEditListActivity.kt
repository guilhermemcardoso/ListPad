package me.gmcardoso.listpad

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private fun updateUI() {
         categories = categoryDAO.categories()

        var categoryAdapter: ArrayAdapter<Category> = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityCreateOrEditListBinding.spCategories.adapter = categoryAdapter
    }
}