package org.zornco.ra_playlist_maker.libretro

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.zornco.ra_playlist_maker.common.DataHolder
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class PlaylistLoader {
    companion object {
        fun savePlaylist(playlist:JsonClasses.RAPlaylist){
            val gson = GsonBuilder().setPrettyPrinting().create()
            val file = File(playlist.PATH)
            file.writeText(gson.toJson(playlist))
        }

        @Throws(Exception::class)
        fun loadPlaylist(path:String ): JsonClasses.RAPlaylist {
            val gson = Gson()
            try {
                val json = loadJSONFromAsset(path)
                return gson.fromJson(json, JsonClasses.RAPlaylist::class.java)
            }
            catch (e:Exception)
            {
                throw e
            }

        }

        @Throws(IOException::class)
        fun loadJSONFromAsset(file: String): String? {
            val json: String?
            try {
                val fil = File(file)
                val iStream = fil.inputStream()
                val size = iStream.available()
                val buffer = ByteArray(size)
                iStream.read(buffer)
                iStream.close()
                json = String(buffer, Charset.defaultCharset())
            } catch (ex: IOException) {
                throw ex
            }

            return json
        }
    }
}