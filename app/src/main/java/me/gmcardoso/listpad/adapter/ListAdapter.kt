package me.gmcardoso.listpad.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.gmcardoso.listpad.R
import me.gmcardoso.listpad.model.Category
import me.gmcardoso.listpad.model.List

class ListAdapter(val lists: ArrayList<List>, val categories: ArrayList<Category>): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    var listener: ListListener? = null

    fun setOnClickListener(listener: ListListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {
        val categoryId = lists[position].categoryId
        val category = categories.find { it.id == categoryId }
        val color = Color.parseColor("#"+ category!!.color)

        holder.nameVH.text = lists[position].name
        holder.categoryVH.text = category.name
        holder.descriptionVH.text = lists[position].description
        holder.listColorVH.setBackgroundColor(color)
        holder.categoryTagVH.setBackgroundColor(color)

        if(lists[position].urgent == 1) {
            holder.urgentVH.visibility = View.VISIBLE
        } else {
            holder.urgentVH.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameVH = view.findViewById<TextView>(R.id.tvListName)
        val categoryVH = view.findViewById<TextView>(R.id.tvListCategory)
        val descriptionVH = view.findViewById<TextView>(R.id.tvListDescription)
        val listColorVH = view.findViewById<View>(R.id.vListColor)
        val categoryTagVH = view.findViewById<LinearLayout>(R.id.llCategoryTag)
        val urgentVH = view.findViewById<ImageView>(R.id.ivUrgentIcon)

        init {
            view.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface ListListener {
        fun onItemClick(pos: Int)
    }
}