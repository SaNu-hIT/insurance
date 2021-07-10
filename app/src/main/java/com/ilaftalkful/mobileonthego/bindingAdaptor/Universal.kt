package com.ilaftalkful.mobileonthego.bindingAdaptor

interface Universal<T> {
     fun addAll(items: Collection<T>)

     fun add(item: T)

     fun remove(item: T, position: Int)

     fun removeRange(vararg items: T)

     fun update(item: T)

     fun updateRange(vararg items: T)

     fun clearAndAddAll(collection: Collection<T>)
}
