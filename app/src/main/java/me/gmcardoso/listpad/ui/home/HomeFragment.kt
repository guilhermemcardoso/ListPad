package me.gmcardoso.listpad.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.gmcardoso.listpad.CreateOrEditListActivity
import me.gmcardoso.listpad.ListActivity
import me.gmcardoso.listpad.adapter.ListAdapter
import me.gmcardoso.listpad.database.DatabaseHandler
import me.gmcardoso.listpad.database.dao.CategoryDAO
import me.gmcardoso.listpad.database.dao.ListDAO
import me.gmcardoso.listpad.databinding.FragmentHomeBinding
import me.gmcardoso.listpad.model.List

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var listDAO: ListDAO
    private lateinit var categoryDAO: CategoryDAO
    private var lists = ArrayList<List>()
    private lateinit var listAdapter: ListAdapter
    private lateinit var homeContext: Context

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnAdd.setOnClickListener {
            val intent = Intent(homeContext, CreateOrEditListActivity::class.java)
            startActivity(intent)
        }

        databaseHandler = DatabaseHandler(homeContext)
        categoryDAO = CategoryDAO(databaseHandler)
        listDAO = ListDAO(databaseHandler)

        updateUI()

        return root
    }

    private fun updateUI() {
        lists = listDAO.lists()
        val categories = categoryDAO.categories()

        listAdapter = ListAdapter(lists, categories)

        binding.rvLists.layoutManager = LinearLayoutManager(homeContext)
        binding.rvLists.adapter = listAdapter

        val listener = object: ListAdapter.ListListener {
            override fun onItemClick(pos: Int) {
                val list = lists[pos]
                val intent = Intent(context, ListActivity::class.java)
                intent.putExtra("listId", list.id)
                intent.putExtra("listName", list.name)
                startActivity(intent)
            }
        }

        listAdapter.setOnClickListener(listener)
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