package org.zornco.ra_playlist_maker.Libretro

import android.content.Context
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset

class Playlist {
    companion object {
        fun load(context: Context): JSONArray {
            val jsonObj = JSONArray(
                loadJSONFromAsset(
                    context,
                    "systems.json"
                )
            )
            return jsonObj
        }

        fun loadJSONFromAsset(context: Context, file: String): String? {
            var json: String?
            try {
                val iStream = context.assets.open(file)
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