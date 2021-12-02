package me.gmcardoso.listpad.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import me.gmcardoso.listpad.R
import me.gmcardoso.listpad.model.Color
import android.graphics.Color as GraphicsColor

class ColorAdapter(val colors: ArrayList<Color>, val context: Context): RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    var listener: ColorListener? = null
    var selectedPos: Int = RecyclerView.NO_POSITION

    fun setOnClickListener(listener: ColorListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorAdapter.ColorViewHolder, position: Int) {
        val color = GraphicsColor.parseColor("#"+ colors[position].hex)
        holder.colorVH.setCardBackgroundColor(color)

        if(selectedPos == position) {
            holder.colorLayoutVH.setBackgroundColor(android.graphics.Color.parseColor("#123456"))
        } else {
            holder.colorLayoutVH.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    inner class ColorViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val colorVH = view.findViewById<CardView>(R.id.cvColor)
        val colorLayoutVH = view.findViewById<LinearLayout>(R.id.llColor)

        init {
            view.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface ColorListener {
        fun onItemClick(pos: Int)
    }
}