package org.zornco.ra_playlist_maker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.zornco.ra_playlist_maker.databinding.FragmentFileBrowserBinding

/**
 * A simple [Fragment] subclass.
 */
class FileBrowserFragment : Fragment() {
    private lateinit var binding : FragmentFileBrowserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_browser,container, false);

        return binding.root
    }

}
