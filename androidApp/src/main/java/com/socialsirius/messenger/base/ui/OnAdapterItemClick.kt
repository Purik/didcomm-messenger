package com.socialsirius.messenger.base.ui

interface OnAdapterItemClick<T> {
    fun onItemClick(item: T)
}

interface OnCustomBtnClick<T> {
    fun onBtnClick(btnId : Int, item: T?,  position : Int)
}