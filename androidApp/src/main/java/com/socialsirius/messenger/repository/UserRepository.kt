package com.socialsirius.messenger.repository

import androidx.annotation.WorkerThread
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppExecutors
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.models.User

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val appExecutors: AppExecutors)  {

    var myUser = User()

    init {
        setupUserFromPref()
    }

    fun setupUserFromPref(){
        val user =  AppPref.getInstance().getUser()
        user?.let {
            myUser = it
        }
    }

    fun saveUserToPref(){
        AppPref.getInstance().setUser(myUser)
    }

    fun logout(){
        AppPref.getInstance().setUser(null)
        AppPref.getInstance().setPin(null)
        AppPref.getInstance().setUseBiometric(false)
        myUser = User()
            //TODO remove all data from db
    }

}