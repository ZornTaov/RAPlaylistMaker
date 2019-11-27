package org.zornco.ra_playlist_maker.libretro

import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class PlaylistLoader {
    companion object {
        fun loadPlaylist(context: Context, path:String ): List<JsonClasses.RAPlaylistEntry> {
            val gson = Gson()
            return gson.fromJson(loadJSONFromAsset(context, path), JsonClasses.RAPlaylist::class.java).items
        }

        fun loadJSONFromAsset(context: Context, file: String): String? {
            var json: String?
            try {
                val fil = File(file)
                val iStream = fil.inputStream()
                val size = iStream.available()
                val buffer = ByteArray(size)
                iStream.read(buffer)
                iStream.close()
                json = String(buffer, Charset.defaultCharset())
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }
    }
}