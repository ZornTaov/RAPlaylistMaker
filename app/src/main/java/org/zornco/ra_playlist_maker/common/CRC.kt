package org.zornco.ra_playlist_maker.common

import java.io.File
import java.util.zip.CRC32

class CRC {
    companion object {
        fun getCRC(path: String): Long {
            val romFile = File(path)

            val buffer = ByteArray(1024*1024)
            val crc = CRC32()
            romFile.inputStream().buffered(1024*1024).use { input ->
                with(crc) {
                    var mByte = 0
                    do {
                        val sz = input.read(buffer)
                        if (sz <= 0)
                            break
                        update(buffer)
                        println("The CRC-32 checksum is ${"%X".format(value)}")
                        mByte++
                    } while (mByte < 64)
                }
            }
            return crc.value
        }
    }
}