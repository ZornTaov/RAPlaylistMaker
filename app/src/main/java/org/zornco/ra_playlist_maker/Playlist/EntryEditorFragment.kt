package org.zornco.ra_playlist_maker.Playlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.zornco.ra_playlist_maker.R

/**
 * A simple [Fragment] subclass.
 */
class EntryEditorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_editor, container, false)
    }


}
