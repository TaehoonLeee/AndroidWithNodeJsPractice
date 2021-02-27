package com.example.nodejs.Util

import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class BaseObservable<LISTENER_CLASS> {
    private val listeners = Collections.newSetFromMap(
        ConcurrentHashMap<LISTENER_CLASS, Boolean>(1)
    )

    fun registerListener(listener : LISTENER_CLASS) {
        listeners.add(listener)
    }

    fun unregisterListener(listener : LISTENER_CLASS) {
        listeners.remove(listener)
    }

    fun getListeners() : Set<LISTENER_CLASS> {
        return listeners
    }
}