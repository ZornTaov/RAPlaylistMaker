package org.zornco.ra_playlist_maker.common

import org.zornco.ra_playlist_maker.Libretro.JsonClasses

interface OnItemClickListener<T> {
    fun onClick(fileModel: T)

    fun onLongClick(fileModel: T)
}

fun getSystemName(fileModel: JsonClasses.RASystem): String {
    return if (fileModel.system[0].isEmpty()) fileModel.name else fileModel.system[0]
}