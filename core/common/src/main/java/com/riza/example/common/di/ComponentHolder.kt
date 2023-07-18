package com.riza.example.common.di

object ComponentHolder {
    var daggerComponents: MutableMap<Class<*>, ComponentFactory<*>> = mutableMapOf()

    inline fun <reified T> register(componentFactory: ComponentFactory<T>) {
        daggerComponents[T::class.java] = componentFactory
    }

    fun <E> getComponent(key: Class<E>): ComponentFactory<*>? {
        return daggerComponents[key]
    }
}
