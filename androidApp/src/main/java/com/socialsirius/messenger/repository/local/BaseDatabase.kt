package com.socialsirius.messenger.repository.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.stmt.DeleteBuilder
import com.j256.ormlite.stmt.QueryBuilder
import com.j256.ormlite.stmt.SelectArg
import com.j256.ormlite.stmt.UpdateBuilder
import com.socialsirius.messenger.repository.models.DatabaseIdModel
import java.sql.SQLException

abstract class BaseDatabase<T, M>(ctx: Context?) {

    var db: DatabaseHelper? = null

    init {
        try {
            val dbManager = DatabaseManager()
            db = dbManager.getHelper(ctx)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun getMainDao(): Dao<T, M>

    fun isExist(item: T): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        try {
            if (item is DatabaseIdModel) {
                val exist = getMainDao()!!.idExists(item.getId() as? M)
                liveData.postValue(exist)
                return liveData
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        liveData.postValue(false)
        return liveData
    }


    fun create(item: T) {
        try {
            getMainDao()!!.create(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createOrUpdate(item: T) {
        try {
            getMainDao().createOrUpdate(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeBy(item: T? = null, id: M? = null) {
        try {
            if (item != null) {
                getMainDao().delete(item)
            } else if (id != null) {
                getMainDao().deleteById(id)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getItemById(id: M): LiveData<T?> {
        val liveData = MutableLiveData<T?>()
        try {
            val item = getMainDao().queryForId(id)
            liveData.postValue(item)
            return liveData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        liveData.postValue(null)
        return liveData
    }

    fun getItemBy(id: M): T? {
        try {
            val item = getMainDao().queryForId(id)
            return item
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    abstract fun deleteAll()


    fun getAll(): LiveData<List<T>> {
        val liveData = MutableLiveData<List<T>>()
        try {
            val list = getMainDao().queryForAll()
            liveData.postValue(list.orEmpty())
            return liveData
        } catch (throwables: Exception) {
            throwables.printStackTrace()
        }
        liveData.postValue(listOf())
        return liveData
    }

    fun getItemsBy(column: String, query: Any): LiveData<List<T>> {
        val liveData = MutableLiveData<List<T>>()
        try {
            val list = getMainDao().queryForEq(column, query)
            liveData.postValue(list.orEmpty())
            return liveData
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        liveData.postValue(listOf())
        return liveData
    }

    fun getItemsBy(args: Map<String, Any>): LiveData<List<T>> {
        val liveData = MutableLiveData<List<T>>()
        try {
            val builder = getQueryeBuilder()
            val wherBuilder = builder.where()
            var i = 0
            args.forEach {
                wherBuilder.eq(it.key, it.value)
                i++
                if(i < args.count()){
                    wherBuilder.and()
                }
            }
            val list =  wherBuilder.query()
            liveData.postValue(list.orEmpty())
            return liveData
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        liveData.postValue(listOf())
        return liveData
    }


    fun createSelectedArgs(`object`: Any?): SelectArg? {
        val selectArg = SelectArg()
        selectArg.setValue(`object`)
        return selectArg
    }


    fun deleteAllFor(column: String, query: Any) {
        try {
            val builder = getDeleteBuilder()
            builder.where().eq(column, query)
            builder.delete()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }

    fun getQueryeBuilder(): QueryBuilder<T, M> {
        val builder = getMainDao().queryBuilder()
        return builder
    }

    fun getUpdateBuilder(): UpdateBuilder<T, M> {
        val builder = getMainDao().updateBuilder()
        return builder
    }

    fun getDeleteBuilder(): DeleteBuilder<T, M> {
        val builder = getMainDao().deleteBuilder()
        return builder
    }
}