package org.zornco.ra_playlist_maker.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.zornco.ra_playlist_maker.BR
import org.zornco.ra_playlist_maker.common.CRC
import org.zornco.ra_playlist_maker.common.DataHolder
import org.zornco.ra_playlist_maker.common.PlaylistState
import org.zornco.ra_playlist_maker.databinding.FragmentEntryEditorBinding

class EntryEditorFragment : Fragment() {
    lateinit var binding: FragmentEntryEditorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEntryEditorBinding.inflate(inflater, container, false)
        binding.entry = DataHolder.currentEntry
        if(binding.entry!!.db_name.isEmpty())
            binding.entry!!.db_name = DataHolder.currentPlaylist!!.PATH.substringAfterLast('/')
        binding.doneButton.setOnClickListener{onDoneClick() }
        binding.crcButton.setOnClickListener{onCRCClick() }
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun onDoneClick() {

        when (DataHolder.currentState)
        {
            PlaylistState.FIRST -> {
                DataHolder.currentPlaylist!!.items.add(binding.entry!!)
                DataHolder.playlistIndex++
            }
            PlaylistState.ADD -> DataHolder.currentPlaylist!!.items.add(binding.entry!!)
            PlaylistState.EDIT -> DataHolder.currentPlaylist!!.items[DataHolder.playlistIndex] = binding.entry!!
        }
        this.activity!!.finish()
    }

    private fun onCRCClick() {
        binding.apply { entry?.crc32 = "${"%X".format(CRC.getCRC(binding.entry!!.path))}|crc" }
        binding.notifyPropertyChanged(BR.entry)
        binding.invalidateAll()
        Toast.makeText(this.activity, "CRC Updated!", Toast.LENGTH_SHORT).show()
    }
}
