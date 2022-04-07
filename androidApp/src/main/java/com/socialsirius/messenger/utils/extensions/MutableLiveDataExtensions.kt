package com.socialsirius.messenger.utils.extensions

import androidx.lifecycle.LiveData

import androidx.lifecycle.Observer
import com.socialsirius.messenger.base.data.models.Resource
import com.socialsirius.messenger.base.data.models.Status
import com.socialsirius.messenger.base.ui.BaseViewModel


fun <T> LiveData<T>.observeOnce(viewModel: BaseViewModel, action: (T) -> Unit) {
    var destroyObserver: Observer<Boolean>? = null
    var observer: Observer<T>? = null

    destroyObserver = Observer<Boolean> { _ ->
        destroyObserver?.let { viewModel.onDestroyLiveData.removeObserver(it) }
        observer?.let { this.removeObserver(it) }
    }

    observer = Observer<T> { value ->
        observer?.let { this.removeObserver(it) }
        action(value)
    }

    viewModel.onDestroyLiveData.observeForever(destroyObserver)
    this.observeForever(observer)
}

fun <T> LiveData<T>.observeUntilDestroy(viewModel: BaseViewModel, action: (T) -> Unit) {
    var destroyObserver: Observer<Boolean>? = null
    var observer: Observer<T>? = null

    destroyObserver = Observer<Boolean> { _ ->
        destroyObserver?.let { viewModel.onDestroyLiveData.removeObserver(it) }
        observer?.let { this.removeObserver(it) }
    }

    observer = Observer<T> { value ->
        action(value)
    }

    viewModel.onDestroyLiveData.observeForever(destroyObserver)
    this.observeForever(observer)
}

fun <T> LiveData<T>.observeOnceUnsafe(action: (T) -> Unit) {
    var observer: Observer<T>? = null

    observer = Observer<T> { value ->
        observer?.let { this.removeObserver(it) }
        action(value)
    }

    this.observeForever(observer)
}

fun <T> LiveData<Resource<T>>.observeOnceForDoneUnsafe(action: (Resource<T>) -> Unit) {
    var observer: Observer<Resource<T>>? = null

    observer = Observer<Resource<T>> { value ->
        if(value.status!= Status.LOADING){
            observer?.let { this.removeObserver(it) }
            action(value)
        }
    }
    this.observeForever(observer)
}

fun <T> LiveData<Resource<T>>.observeOnceForDoneWithLoadingUnsafe(action: (Resource<T>) -> Unit) {
    var observer: Observer<Resource<T>>? = null

    observer = Observer<Resource<T>> { value ->
        if(value.status == Status.SUCCESS || value.status == Status.ERROR){
            observer?.let { this.removeObserver(it) }
        }
        action(value)
    }
    this.observeForever(observer)
}

fun <T> LiveData<Resource<T>>.observeOnceForDone(viewModel: BaseViewModel, action: (Resource<T>) -> Unit) {
    var destroyObserver: Observer<Boolean>? = null
    var observer: Observer<Resource<T>>? = null

    destroyObserver = Observer<Boolean> { _ ->
        destroyObserver?.let { viewModel.onDestroyLiveData.removeObserver(it) }
        observer?.let { this.removeObserver(it) }
    }

    observer = Observer<Resource<T>> { value ->
        observer?.let {
            if (value.status != Status.LOADING) {
                this.removeObserver(it)
            }
            action(value)
        }

    }

    viewModel.onDestroyLiveData.observeForever(destroyObserver)
    this.observeForever(observer)
}