package org.zornco.ra_playlist_maker.common


class BackStackManager<T>
{
    private var stack = mutableListOf<T>()
    var onStackChangeListener: ((List<T>) -> Unit)? = null

    val top:T
        get() = stack[stack.size - 1]

    fun addToStack(t: T)
    {
        stack.add(t)
        onStackChangeListener?.invoke(stack)
    }

    fun popFromStack()
    {
        if (stack.isNotEmpty())
        {
            stack.removeAt(stack.size - 1)
        }
        onStackChangeListener?.invoke(stack)
    }
    fun popFromStackTill(t: T)
    {
        stack = stack.subList(0,stack.indexOf(t)+1)
        onStackChangeListener?.invoke(stack)
    }
}