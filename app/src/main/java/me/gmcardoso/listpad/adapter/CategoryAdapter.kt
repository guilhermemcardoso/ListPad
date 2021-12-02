package me.gmcardoso.listpad.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import me.gmcardoso.listpad.R
import me.gmcardoso.listpad.model.Category

class CategoryAdapter(val categories: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var listener: CategoryListener? = null

    fun setOnClickListener(listener: CategoryListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        holder.nameVH.text = categories[position].name

        val color = Color.parseColor("#"+ categories[position].color)
        holder.categoryVH.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameVH = view.findViewById<TextView>(R.id.tvCategoryName)
        val categoryVH = view.findViewById<CardView>(R.id.cvCategory)

        init {
            view.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface CategoryListener {
        fun onItemClick(pos: Int)
    }
}