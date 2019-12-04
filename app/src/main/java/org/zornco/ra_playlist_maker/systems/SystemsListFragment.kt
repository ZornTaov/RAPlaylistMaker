package org.zornco.ra_playlist_maker.systems


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_systems_list.*
import org.zornco.ra_playlist_maker.libretro.CoreInfoParser
import org.zornco.ra_playlist_maker.libretro.JsonClasses
import org.zornco.ra_playlist_maker.MainActivity
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import org.zornco.ra_playlist_maker.common.getSystemName
import java.lang.Exception

class SystemsListFragment : Fragment() {

    private lateinit var mFilesAdapter: SystemsRecyclerAdapter
    private lateinit var mCallback: OnItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mCallback = context as OnItemClickListener
        }catch (e: Exception)
        {
            throw  Exception("$context should implement SystemListFragment.OnItemClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_systems_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        systemsRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = SystemsRecyclerAdapter()
        systemsRecyclerView.adapter = mFilesAdapter
        updateDate()
        mFilesAdapter.onItemClickListener = {
            mCallback.onClick(it)
        }
        mFilesAdapter.onItemLongClickListener = {
            mCallback.onLongClick(it)
        }
    }

    private fun updateDate() {
        val files = getSystemsFromDatabase(CoreInfoParser.cacheSystems(this.activity as MainActivity))

        if (files.isEmpty()) {
            emptySystemsLayout.visibility = View.VISIBLE
        } else {
            emptySystemsLayout.visibility = View.INVISIBLE
        }

        mFilesAdapter.updateData(files.sortedBy { getSystemName(it) })
    }

    private fun getSystemsFromDatabase(systems : MutableMap<String, JsonClasses.RASystem>): List<JsonClasses.RASystem> {
        return systems.map {
            JsonClasses.RASystem(
                it.value.name,
                it.value.cores,
                it.value.system,
                it.value.allExt,
                it.value.systemExt
            )
        }
    }


}
