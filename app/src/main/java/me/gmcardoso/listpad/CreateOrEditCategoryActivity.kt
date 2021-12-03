package me.gmcardoso.listpad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import me.gmcardoso.listpad.adapter.ColorAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.CategoryDAO
import me.gmcardoso.listpad.database.dao.ColorDAO
import me.gmcardoso.listpad.databinding.ActivityCreateOrEditCategoryBinding
import me.gmcardoso.listpad.model.Category
import me.gmcardoso.listpad.model.Color
import android.app.AlertDialog
import me.gmcardoso.listpad.database.dao.ListDAO


class CreateOrEditCategoryActivity : AppCompatActivity() {

    private lateinit var activityCreateOrEditCategoryBinding: ActivityCreateOrEditCategoryBinding
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var listDAO: ListDAO
    private lateinit var colorDAO: ColorDAO
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var colors: ArrayList<Color>
    private lateinit var colorAdapter: ColorAdapter
    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateOrEditCategoryBinding = ActivityCreateOrEditCategoryBinding.inflate(layoutInflater)
        setContentView(activityCreateOrEditCategoryBinding.root)

        databaseHandler = DatabaseHandler(this)
        categoryDAO = CategoryDAO(databaseHandler)
        listDAO = ListDAO(databaseHandler)
        colorDAO = ColorDAO(databaseHandler)

        activityCreateOrEditCategoryBinding.btnCancel.setOnClickListener {
            cancel()
        }

        activityCreateOrEditCategoryBinding.btnSave.setOnClickListener {
            save()
        }

        updateUI()
    }

    private fun cancel() {
        finish()
    }

    private fun save() {
        val categoryName = activityCreateOrEditCategoryBinding.etCategoryName.text.toString()
        val categoryColor = colors[colorAdapter.selectedPos]

        if(this::category.isInitialized) {
            category.name = categoryName
            category.color = categoryColor.hex
            categoryDAO.update(category)
        } else {
            val newCategory = Category(null, categoryName, 0, categoryColor.hex)
            categoryDAO.save(newCategory)
        }

        finish()
    }

    private fun updateUI() {
        colors = colorDAO.colors()
        colorAdapter = ColorAdapter(colors, this)

        activityCreateOrEditCategoryBinding.rvColors.layoutManager = GridLayoutManager(this, 6)
        activityCreateOrEditCategoryBinding.rvColors.adapter = colorAdapter

        val categoryId = this.intent.getIntExtra("categoryId", 0)

        if(categoryId != 0) {
            category = categoryDAO.get(categoryId)
            if(category.default == 1) {
                activityCreateOrEditCategoryBinding.ivDeleteIcon.visibility = View.GONE
            }
        } else {
            activityCreateOrEditCategoryBinding.ivDeleteIcon.visibility = View.GONE
        }

        if(this::category.isInitialized) {
            setCategoryFields(category)
        }

        val listener = object: ColorAdapter.ColorListener {
            override fun onItemClick(pos: Int) {
                val oldItemSelected = colorAdapter.selectedPos
                colorAdapter.selectedPos = pos
                colorAdapter.notifyItemChanged(oldItemSelected)
                colorAdapter.notifyItemChanged(pos)
            }
        }

        colorAdapter.setOnClickListener(listener)
        activityCreateOrEditCategoryBinding.ivDeleteIcon.setOnClickListener { showDeleteDialog() }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_category))
            .setMessage(getString(R.string.delete_category_message))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ -> deleteCategory() }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteCategory() {
        if(this::category.isInitialized) {
            val itemLists = listDAO.getByCategory(category.id!!)

            if(itemLists.size > 0) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.unavailable_action))
                    .setMessage(getString(R.string.cant_delete_category_message))
                    .setNeutralButton(getString(R.string.understood), null)
                    .show()
            }

            if(itemLists.size == 0) {
                category.id?.let { categoryDAO.delete(it) }
                finish()
            }
        }
    }

    private fun setCategoryFields(category: Category) {
        activityCreateOrEditCategoryBinding.etCategoryName.setText(category.name)
        if(category.default == 1) {
            activityCreateOrEditCategoryBinding.etCategoryName.isEnabled = false
        }
        colorAdapter.selectedPos = colors.indexOfFirst { it.hex == category.color }
        colorAdapter.notifyItemChanged(colorAdapter.selectedPos)

    }
}