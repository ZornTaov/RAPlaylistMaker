package org.zornco.ra_playlist_maker.playlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.zornco.ra_playlist_maker.common.DataHolder
import org.zornco.ra_playlist_maker.databinding.FragmentEntryEditorBinding
import org.zornco.ra_playlist_maker.libretro.JsonClasses

class EntryEditorFragment : Fragment() {
    lateinit var binding: FragmentEntryEditorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEntryEditorBinding.inflate(inflater, container, false)
        binding.entry = DataHolder.currentEntry
        return binding.root
    }


}
