package com.gamapp.sportshopapplication

open class SingletonHolder<out T, in A, B, C>(creator: (A, B, C) -> T) {
    private var creator: ((A, B, C) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg1: A, arg2: B, arg3: C): T {
        val checkInstance = instance
        if (checkInstance != null) {
            return checkInstance
        }

        return synchronized(this) {
            val checkInstanceAgain = instance
            if (checkInstanceAgain != null) {
                checkInstanceAgain
            } else {
                val created = creator!!(arg1, arg2, arg3)
                instance = created
                creator = null
                created
            }
        }
    }


    fun getInstance(): T? {
        val checkInstance = instance
        if (instance != null)
            return checkInstance
        else return null
    }
}