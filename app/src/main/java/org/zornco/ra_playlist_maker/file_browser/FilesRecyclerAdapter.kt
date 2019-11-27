package org.zornco.ra_playlist_maker.file_browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler_file.view.*
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.FileModel
import org.zornco.ra_playlist_maker.common.FileType

class FilesRecyclerAdapter : RecyclerView.Adapter<FilesRecyclerAdapter.ViewHolder>() {

    var onItemClickListener: ((FileModel) -> Unit)? = null
    var onItemLongClickListener: ((FileModel) -> Unit)? = null

    var filesList = listOf<FileModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(filesList: List<FileModel>, extensions: List<String>) {
        this.filesList = filesList.filter { it.fileType == FileType.FOLDER
                || it.fileType == FileType.FILE && extensions.isEmpty()
                || extensions.isNotEmpty() && extensions.contains(it.extension) }

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener?.invoke(filesList[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            onItemLongClickListener?.invoke(filesList[adapterPosition])
            return true
        }

        fun bindView(position: Int) {
            val fileModel = filesList[position]
            itemView.nameTextView.text = fileModel.name

            if (fileModel.fileType == FileType.FOLDER) {
                itemView.folderTextView.visibility = View.VISIBLE
                itemView.totalSizeTextView.visibility = View.GONE
                itemView.folderTextView.text = "(${fileModel.subFiles} files)"
                itemView.setBackgroundColor(0xEEEEEE)
            } else {
                itemView.folderTextView.visibility = View.GONE
                itemView.totalSizeTextView.visibility = View.VISIBLE
                itemView.totalSizeTextView.text = "${String.format("%.2f", fileModel.sizeInMB)} mb"
            }
        }
    }
}