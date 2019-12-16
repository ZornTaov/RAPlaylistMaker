package org.zornco.ra_playlist_maker.common

import java.io.File

class FileUtils
{
    companion object {
        fun getFilesFromPath(
            path: String,
            showHiddenFiles: Boolean = false,
            onlyFolders: Boolean = false
        ): List<File> {
            val file = File(path)
            return file.listFiles()
                .filter { showHiddenFiles || !it.name.startsWith(".") }
                .filter { !onlyFolders || it.isDirectory }
                .toList()
        }

        fun getFileModelsFromFiles(files: List<File>): List<FileModel> {
            return files.map {
                FileModel(
                    it.path,
                    FileType.getFileType(it),
                    it.name,
                    convertFileSizeToMB(it.length()),
                    it.extension,
                    it.listFiles()?.size
                        ?: 0
                )
            }
        }

        private fun convertFileSizeToMB(sizeInBytes: Long): Double {
            return (sizeInBytes.toDouble()) / (1024 * 1024)
        }
    }
}