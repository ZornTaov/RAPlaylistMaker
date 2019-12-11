package org.zornco.ra_playlist_maker.systems


import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import org.zornco.ra_playlist_maker.libretro.JsonClasses

import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.databinding.FragmentSystemsBinding
import org.zornco.ra_playlist_maker.common.OnItemClickListener

class SystemsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSystemsBinding
    //private val backStackManager = BackStackManager<JsonClasses.RASystem>()
    //private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<JsonClasses.RASystem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_systems, container, false)
        initViews()
        initBackStack()
        return binding.root
    }
    private fun initViews()
    {
        (this.activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

//        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        mBreadcrumbRecyclerAdapter =
//            BreadcrumbRecyclerAdapter()
//        binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
//        mBreadcrumbRecyclerAdapter.onItemClickListener = {
//            this.activity?.supportFragmentManager?.popBackStack(it.name, 2)
//            backStackManager.popFromStackTill(it)
//        }
    }

    private fun initBackStack()
    {
//        backStackManager.onStackChangeListener = {
//            updateAdapterData(it)
//        }
//        backStackManager.addToStack(fileModel = JsonClasses.RASystem("Systems"))
    }

    override fun onClick(obj: Any) {

        val systemModel = obj as JsonClasses.RASystem
        Log.d("TAG", "${systemModel.name}")
        val action = SystemsFragmentDirections.actionSystemsFragmentToPlaylistFragment(systemModel)
        this.view!!.findNavController().navigate(action)
    }

    override fun onLongClick(obj: Any) {

    }
}
