package org.zornco.ra_playlist_maker.systems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import org.zornco.ra_playlist_maker.libretro.JsonClasses

import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.DataHolder
import org.zornco.ra_playlist_maker.databinding.FragmentSystemsBinding
import org.zornco.ra_playlist_maker.common.OnItemClickListener

class SystemsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSystemsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_systems, container, false)
        initViews()
        return binding.root
    }

    private fun initViews()
    {
        (this.activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    override fun onClick(obj: Any) {
        val systemModel = obj as JsonClasses.RASystem
        DataHolder.currentSystem = systemModel
        val action = SystemsFragmentDirections.actionSystemsFragmentToPlaylistFragment()
        this.view!!.findNavController().navigate(action)
    }

    override fun onLongClick(obj: Any) {

    }
}
