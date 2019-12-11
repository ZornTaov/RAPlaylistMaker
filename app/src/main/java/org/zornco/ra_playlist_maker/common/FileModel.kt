package org.zornco.ra_playlist_maker.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FileModel(
    val path: String,
    val fileType: FileType,
    val name: String,
    val sizeInMB: Double,
    val extension: String = "",
    val subFiles: Int = 0
) :Parcelable