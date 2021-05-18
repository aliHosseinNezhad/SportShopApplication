package com.gamapp.sportshopapplication.utils

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class Observable<T>(default: T) {
    var underlying = default
        set(value) {
            // Trigger on change
        }

    fun addObserver(onChange: (old: T, new: T) -> Unit) {
    /*etc*/
    }

    fun removeObserver(onChange: (old: T, new: T) -> Unit) {
    /*etc*/
    }

    operator fun getValue(thisRef: Any, prop: KProperty<*>): T = underlying
    operator fun setValue(thisRef: Any, prop: KProperty<*>, value: T) {
        underlying = value
    }
}
