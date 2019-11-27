package org.zornco.ra_playlist_maker.common

import org.zornco.ra_playlist_maker.libretro.JsonClasses

interface OnItemClickListener {
    fun onClick(obj: Any)

    fun onLongClick(obj: Any)
}

fun getSystemName(fileModel: JsonClasses.RASystem): String {
    return if (fileModel.system[0].isEmpty()) fileModel.name else fileModel.system[0]
}