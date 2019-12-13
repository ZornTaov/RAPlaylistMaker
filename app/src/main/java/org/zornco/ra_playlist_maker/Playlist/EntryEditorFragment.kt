package org.zornco.ra_playlist_maker.playlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.gson.Gson
import org.zornco.ra_playlist_maker.MainActivity
import org.zornco.ra_playlist_maker.common.DataHolder
import org.zornco.ra_playlist_maker.databinding.FragmentEntryEditorBinding
import java.io.File
import java.io.FileWriter

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
        return binding.root
    }
    private fun onDoneClick() {
        if (DataHolder.currentPlaylist!!.items.isEmpty()) {
            DataHolder.currentPlaylist!!.items.add(binding.entry!!)
            DataHolder.playlistIndex++
        }
        else {
            if (this.activity is MainActivity)
                DataHolder.currentPlaylist!!.items[DataHolder.playlistIndex] = binding.entry!!
            else
                DataHolder.currentPlaylist!!.items.add(binding.entry!!)
        }
        val gson = Gson()
        val file = File(DataHolder.currentPlaylist!!.PATH)
        file.writeText(gson.toJson(DataHolder.currentPlaylist))
        file.writer().close()
        try {
            val action = EntryEditorFragmentDirections.actionEntryEditorFragmentToPlaylistFragment()
            this.view!!.findNavController().navigate(action)
        }catch (ex: Exception)
        {
            this.activity!!.finish()
        }
    }

}
