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
import androidx.recyclerview.widget.LinearLayoutManager
import org.zornco.ra_playlist_maker.libretro.JsonClasses

import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.databinding.FragmentSystemsBinding
import org.zornco.ra_playlist_maker.common.BackStackManager
import org.zornco.ra_playlist_maker.common.BreadcrumbRecyclerAdapter
import org.zornco.ra_playlist_maker.common.OnItemClickListener

class SystemsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSystemsBinding
    private val backStackManager =
        BackStackManager<JsonClasses.RASystem>()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<JsonClasses.RASystem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_systems, container, false)
        if (savedInstanceState == null) {
            val filesListFragment =
                SystemsListFragment.build {
                    path = Environment.getExternalStorageDirectory().absolutePath
                }

            this.activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }
        initViews()
        initBackStack()
        return binding.root
    }
    private fun initViews()
    {
        (this.activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        mBreadcrumbRecyclerAdapter =
            BreadcrumbRecyclerAdapter()
        binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
        mBreadcrumbRecyclerAdapter.onItemClickListener = {
            this.activity?.supportFragmentManager?.popBackStack(it.name, 2)
            backStackManager.popFromStackTill(it)
        }
    }

    private fun initBackStack()
    {
        backStackManager.onStackChangeListener = {
            updateAdapterData(it)
        }
        backStackManager.addToStack(fileModel = JsonClasses.RASystem("Systems"))
    }

    private fun updateAdapterData(files: List<JsonClasses.RASystem>) {
        mBreadcrumbRecyclerAdapter.updateData(files)
        if (files.isNotEmpty())
        {
            binding.breadcrumbRecyclerView.smoothScrollToPosition(files.size - 1)
        }
    }
    override fun onClick(obj: Any) {

        val systemModel = obj as JsonClasses.RASystem
        Log.d("TAG", "${systemModel.name}")
        val amount = systemModel.system[0]
        val action = SystemsFragmentDirections.actionSystemsFragmentToPlaylistFragment(amount)
        this.view!!.findNavController().navigate(action)
    }

    override fun onLongClick(obj: Any) {

    }

    private fun addSystemFragment(systemModel: JsonClasses.RASystem)
    {
        val systemsListFragment =
            SystemsListFragment.build {
                path = systemModel.system[0]
            }
        val fragmentTransaction = this.activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, systemsListFragment)
        fragmentTransaction.addToBackStack(systemModel.system[0])
        fragmentTransaction.commit()
    }


}
