package org.zornco.ra_playlist_maker.systems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler_file.view.*
import org.zornco.ra_playlist_maker.libretro.JsonClasses
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.getSystemName

class SystemsRecyclerAdapter : RecyclerView.Adapter<SystemsRecyclerAdapter.ViewHolder>() {

    var onItemClickListener: ((JsonClasses.RASystem) -> Unit)? = null
    var onItemLongClickListener: ((JsonClasses.RASystem) -> Unit)? = null

    var systemsMap = listOf<JsonClasses.RASystem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_system, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = systemsMap.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(filesList: List<JsonClasses.RASystem>) {
        this.systemsMap = filesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener?.invoke(systemsMap[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            onItemLongClickListener?.invoke(systemsMap[adapterPosition])
            return true
        }

        fun bindView(position: Int) {
            val fileModel = systemsMap[position]
            itemView.nameTextView.text = getSystemName(fileModel)
            itemView.setBackgroundColor(0xEEEEEE)
        }
    }
}