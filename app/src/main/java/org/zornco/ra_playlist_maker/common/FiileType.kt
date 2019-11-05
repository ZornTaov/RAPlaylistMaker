package org.zornco.ra_playlist_maker.common

import java.io.File

enum class FileType {
    FILE,
    FOLDER;

    companion object {
        fun getFileType(file: File) = when (file.isDirectory) {
            true -> FOLDER
            false -> FILE
        }
    }
}