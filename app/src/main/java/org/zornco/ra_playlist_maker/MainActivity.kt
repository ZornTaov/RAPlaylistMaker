package org.zornco.ra_playlist_maker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import org.zornco.ra_playlist_maker.Libretro.JsonClasses
import org.zornco.ra_playlist_maker.databinding.ActivityMainBinding
import java.io.*
import java.lang.ref.WeakReference
import java.net.URL
import java.net.URLConnection
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private val STORAGE_PERMISSION_CODE = 101
    private val INTERNET_PERMISSION_CODE = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE)
        checkPermission(
            Manifest.permission.INTERNET,
            INTERNET_PERMISSION_CODE)
        binding.textView.text = Build.SUPPORTED_ABIS[0]
    }

    fun buttonClick(view: View){
        // Get the .index file of cores for the ABI this device uses
        val coreList = createTempFile(this, "index", "txt")
        // only redownload and cache once a day
        if (TimeUnit.DAYS.convert(Calendar.getInstance().time.time - coreList.lastModified(), TimeUnit.MILLISECONDS)> 1) {
            val task =
                DownloadTask(this).execute("http://buildbot.libretro.com/nightly/android/latest/${Build.SUPPORTED_ABIS[0]}/.index")
                    .get()
            var index = 0
            val split = task.toString().split("\n").map {
                if ({ index = it.indexOf("libretro"); index }() > 0)
                    it.subSequence(0, index + "libretro".length)
                else it
            }
            coreList.printWriter().use { out ->
                split.forEach { x ->
                    if(x.isNotBlank())
                        out.println(x)
                }
            }
            task.close()
        }
        val list = coreList.readLines()

        // Get the info.zip of all cores
        val infozip = createTempFile(this, "info", "zip")
        // only redownload and cache once a day
        if (TimeUnit.DAYS.convert(Calendar.getInstance().time.time - infozip.lastModified(), TimeUnit.MILLISECONDS)> 1) {

            val info = DownloadTask(this)
                .execute("http://buildbot.libretro.com/assets/frontend/info.zip")
                .get() as ByteArrayOutputStream
            infozip.outputStream().write(info.toByteArray())
        }
        val zipinfo = ZipFile(infozip)
        val zipInputStream = ZipInputStream(infozip.inputStream())

        // for reach core.info that is in the .index, create a map -> CoreInfo data class
        val coreinfos = mutableListOf<JsonClasses.CoreInfo>()
        while(true){
            val entry = zipInputStream.nextEntry ?: break
            if(list.contains(entry.name.substringBefore('.')))
            {
                val map = emptyMap<String, Any?>().toMutableMap()
                val zipBR = BufferedReader(InputStreamReader(zipinfo.getInputStream(entry)))

                // Get core.info straight from the zip, no need to output to a file
                zipBR.readLines().forEach{
                    if (it.isNotEmpty()) {
                        val lin = it.split(" = ")
                        if (lin.count() > 1) {
                            when (lin[1].toIntOrNull()) {
                                is Int -> {
                                    map[lin[0]] = Integer.parseInt(lin[1])
                                }
                                else -> {
                                    map[lin[0]] = lin[1].subSequence(1, lin[1].length - 1)
                                }
                            }
                        }
                    }
                }
                // sanity checks
                if (map["systemid"] == null)
                    map["systemid"] = "core"
                if (map["database"] == null)
                    map["database"] = ""
                val core = JsonClasses.CoreInfo(map)
                coreinfos.add(core)
                zipBR.close()
            }

        }
        zipinfo.close()
        //if (coreinfos.count() > 0)
        //    Log.d("TAG", "${coreinfos.count()}")

        // from coreinfos, build systems.json
        val systemList = mutableMapOf<String, JsonClasses.RASystem>()
        coreinfos.forEach { core ->
            if (systemList.count() == 0 || systemList[core.systemid] == null) {
                systemList[core.systemid] = JsonClasses.RASystem(
                    core.systemid,
                    mutableListOf(core.corename),
                    core.database.split('|').toMutableList(),
                    core.supported_extensions.split('|').toMutableList(),
                    core.supported_extensions.split('|').toMutableList()
                )
            }
            else
            {
                if (systemList[core.systemid] != null) {
                    systemList[core.systemid]!!.cores.add(core.corename)
                    systemList[core.systemid]!!.system =
                        systemList[core.systemid]!!.system
                            .union(core.database.split('|')).toMutableList()
                    systemList[core.systemid]!!.systemExt =
                        systemList[core.systemid]!!.systemExt
                            .intersect(core.supported_extensions.split('|')).toMutableList()
                    systemList[core.systemid]!!.allExt =
                        systemList[core.systemid]!!.allExt
                            .union(core.supported_extensions.split('|')).toMutableList()
                }
            }
        }
        val gson = Gson()
        val systems = createTempFile(this@MainActivity, "system", "json")
        systems.writeText(gson.toJson(systemList))
        //Log.d("systemList", systemList.count().toString())
    }
    fun InputStream.toFile(path: String) {
        File(path).outputStream().use { this.copyTo(it) }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(permission),
                requestCode
            )
        } /*else {
            Toast.makeText(
                this@FileBrowserActivity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }*/
    }

    private fun createTempFile(context: Context, name: String, type: String): File
    {
        val externalCache = context.externalCacheDir
        val internalCache = context.cacheDir
        val cacheDir: File
        if (externalCache == null && internalCache == null)
            throw IOException("No cache directory")
        cacheDir = when {
            externalCache == null -> internalCache
            internalCache == null -> externalCache
            else -> if (externalCache.freeSpace > internalCache.freeSpace) externalCache else internalCache
        }
        return File(cacheDir, "$name.$type")
    }
    companion object {
        // download buffer size
        const  val BUFFER_SIZE:Int = 8192
        private class DownloadTask(context: MainActivity) : AsyncTask<String, Int, OutputStream>() {
            private val activityReference: WeakReference<MainActivity> = WeakReference(context)

            override fun doInBackground(vararg params: String?): OutputStream {
                // url from param
                val url = URL(params[0])

                // make connection to get length of file
                val connection: URLConnection = url.openConnection()
                connection.connectTimeout = 30000
                connection.readTimeout = 30000
                connection.connect()
                val length: Int = connection.contentLength

                // Get data, push into result
                val result = ByteArrayOutputStream()
                val bis = BufferedInputStream(url.openStream(), BUFFER_SIZE)
                val data = ByteArray(BUFFER_SIZE)
                var total: Long = 0
                var size = 0
                while ({size = bis.read(data); size}() != -1) {
                    total += size.toLong()
                    val percent: Int = ((total * 100) / length).toInt()
                    result.write(data, 0, size)
                    publishProgress(percent)
                }
                // return the stream as String
                return result
            }
            override fun onPreExecute() {
                super.onPreExecute()
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.binding.progressBar.visibility = View.VISIBLE
            }
            override fun onPostExecute(result: OutputStream?) {
                super.onPostExecute(result)
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.binding.progressBar.visibility = View.GONE
            }
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                // we always make sure that the the below operation will not throw null pointer exception
                // other way is use null check like this  //
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return

                val percent = values[0]!!
                activity.binding.progressBar.progress = percent
            }
        }
    }
}
