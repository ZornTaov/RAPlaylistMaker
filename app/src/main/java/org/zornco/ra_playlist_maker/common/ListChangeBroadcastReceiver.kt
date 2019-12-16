package org.zornco.ra_playlist_maker.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ListChangeBroadcastReceiver (val path: String, val onChange: () -> Unit) : BroadcastReceiver()
{
    companion object {
        const val EXTRA_PATH = "org.zornco.ra_playlist_maker.common.path"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val filePath = intent?.extras?.getString(EXTRA_PATH)
        if (filePath.equals(path)) {
            onChange.invoke()
        }
    }
}