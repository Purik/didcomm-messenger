package com.socialsirius.messenger.repository

import androidx.lifecycle.LiveData
import com.socialsirius.messenger.repository.local.BaseDatabase


abstract class BaseRepository<T, M> {

    var databaseHolder: BaseDatabase<T, M>? = null

    open fun getDatabase(): BaseDatabase<T, M> {
        if (databaseHolder == null) {
            databaseHolder = createDatabase()
        }
        return databaseHolder!!
    }

    fun deleteAll(){
        getDatabase().deleteAll()
    }
    abstract fun createDatabase(): BaseDatabase<T, M>

    open fun isExist(item: T): LiveData<Boolean> {
        return getDatabase().isExist(item)
    }

    open fun createItem(item: T) {
        getDatabase().create(item)
    }

    open fun createOrUpdateItem(item: T) {
        getDatabase().createOrUpdate(item)
    }

    open fun getAllItems(): LiveData<List<T>> {
        return getDatabase().getAll()
    }

    open fun getItemsBy(
        column: String, query: Any, orderByColumn: String? = null,
        ascending: Boolean = true
    ): LiveData<List<T>> {
        return getItemsBy(mapOf(Pair(column, query)),orderByColumn,ascending)
    }

    open fun getItemsBy(
        args: Map<String, Any>,
        orderByColumn: String? = null,
        ascending: Boolean = true
    ): LiveData<List<T>> {
        return getDatabase().getItemsBy(args, orderByColumn, ascending)
    }

    open fun getItemById(id: M): LiveData<T?> {
        return getDatabase().getItemById(id)
    }

    open fun getItemBy(id: M): T? {
        return getDatabase().getItemBy(id)
    }

    open fun deleteAllFor(column: String, query: Any) {
        getDatabase().deleteAllFor(column, query)
    }

}