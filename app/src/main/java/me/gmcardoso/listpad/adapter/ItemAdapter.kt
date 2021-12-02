package me.gmcardoso.listpad.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import me.gmcardoso.listpad.R
import me.gmcardoso.listpad.model.Item
import android.view.inputmethod.EditorInfo

class ItemAdapter(private val items: ArrayList<Item>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var listener: ItemListener? = null

    fun setOnClickListener(listener: ItemListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        val item = if(position < itemCount - 1) items[position] else null

        if(position < itemCount - 1) {
            holder.editableDescriptionVH.setText(items[position].description)
            holder.descriptionVH.text = items[position].description
        } else {
            holder.editableDescriptionVH.requestFocus()
            holder.isCompletedVH.visibility = View.INVISIBLE
            holder.deleteIconVH.visibility = View.INVISIBLE
        }

        if(item != null && item.completed == 1) {
            holder.editableDescriptionVH.visibility = View.GONE
            holder.descriptionVH.visibility = View.VISIBLE
            holder.isCompletedVH.isChecked = true
        } else {
            holder.editableDescriptionVH.visibility = View.VISIBLE
            holder.descriptionVH.visibility = View.GONE
            holder.isCompletedVH.isChecked = false
        }
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val editableDescriptionVH: EditText = view.findViewById(R.id.etItemDescription)
        val descriptionVH: TextView = view.findViewById(R.id.tvItemDescription)
        val isCompletedVH: CheckBox = view.findViewById(R.id.cbIsCompleted)
        val deleteIconVH: ImageView = view.findViewById(R.id.ivDeleteIcon)

        init {
            isCompletedVH.setOnClickListener { listener?.onItemCheck(adapterPosition) }
            deleteIconVH.setOnClickListener { listener?.onItemDelete(adapterPosition) }
            editableDescriptionVH.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listener?.onItemDescriptionUpdated(adapterPosition, s.toString(), false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
                })

            editableDescriptionVH.setOnEditorActionListener { textView, actionId, _ ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val itemDescription = textView.text
                    listener?.onItemDescriptionUpdated(adapterPosition, itemDescription.toString(), true)
                    handled = true
                }

                return@setOnEditorActionListener handled
            }
        }
    }

    interface ItemListener {
        fun onItemDescriptionUpdated(pos: Int, description: String, instantly: Boolean)
        fun onItemCheck(pos: Int)
        fun onItemDelete(pos: Int)
    }
}
