package me.gmcardoso.listpad.ui.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.gmcardoso.listpad.CreateOrEditCategoryActivity
import me.gmcardoso.listpad.adapter.CategoryAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.CategoryDAO
import me.gmcardoso.listpad.databinding.FragmentCategoriesBinding
import me.gmcardoso.listpad.model.Category

class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private var _binding: FragmentCategoriesBinding? = null
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var categoryDAO: CategoryDAO
    private var categories = ArrayList<Category>()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var homeContext: Context

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel =
            ViewModelProvider(this).get(CategoriesViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnAdd.setOnClickListener {
            var intent = Intent(homeContext, CreateOrEditCategoryActivity::class.java)
            startActivity(intent)
        }

        databaseHandler = DatabaseHandler(homeContext)
        categoryDAO = CategoryDAO(databaseHandler)

        updateUI()

        return root
    }

    private fun updateUI() {
        categories = categoryDAO.categories()
        categoryAdapter = CategoryAdapter(categories)

        binding.rvCategories.layoutManager = GridLayoutManager(homeContext, 2)
        binding.rvCategories.adapter = categoryAdapter

        val listener = object:CategoryAdapter.CategoryListener {
            override fun onItemClick(pos: Int) {
                val intent = Intent(context, CreateOrEditCategoryActivity::class.java)
                val category = categoryAdapter.categories[pos]
                intent.putExtra("categoryId", category.id)
                startActivity(intent)
            }
        }

        categoryAdapter.setOnClickListener(listener)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}