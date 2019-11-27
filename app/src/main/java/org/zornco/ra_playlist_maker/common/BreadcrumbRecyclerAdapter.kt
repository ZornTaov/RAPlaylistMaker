package org.zornco.ra_playlist_maker.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler_breadcrumb.view.*
import org.zornco.ra_playlist_maker.R

class BreadcrumbRecyclerAdapter<T> : RecyclerView.Adapter<BreadcrumbRecyclerAdapter<T>.ViewHolder>() {

    var onItemClickListener: ((T) -> Unit)? = null

    var files = listOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_breadcrumb, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(files: List<T>) {
        this.files = files
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener?.invoke(files[adapterPosition])
        }

        fun bindView(position: Int) {
            val file = files[position]
            itemView.nameTextView.text = if (file is FileModel) file.name else ""
        }
    }
}