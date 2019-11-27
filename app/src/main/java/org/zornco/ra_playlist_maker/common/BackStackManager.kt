package org.zornco.ra_playlist_maker.common


class BackStackManager<T>
{
    private var files = mutableListOf<T>()
    var onStackChangeListener: ((List<T>) -> Unit)? = null

    val top:T
        get() = files[files.size - 1]

    fun addToStack(fileModel: T)
    {
        files.add(fileModel)
        onStackChangeListener?.invoke(files)
    }

    fun popFromStack()
    {
        if (files.isNotEmpty())
        {
            files.removeAt(files.size - 1)
        }
        onStackChangeListener?.invoke(files)
    }
    fun popFromStackTill(fileModel: T)
    {
        files = files.subList(0,files.indexOf(fileModel)+1)
        onStackChangeListener?.invoke(files)
    }
}