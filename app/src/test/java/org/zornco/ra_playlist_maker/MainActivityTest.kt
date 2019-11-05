package org.zornco.ra_playlist_maker

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

class MainActivityTest {


    fun splitter(): Int
    {
        val str = "display_name = \"2048\""
        val lin = str.split(" = ")
        println(lin[1].subSequence(1, lin[1].length-1).toString())
        return lin.count()
    }
    @Test
    fun splitterTest() {
        assertEquals(2, splitter())
    }
}