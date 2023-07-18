package com.riza.example.common.di

interface ComponentFactory<E> {
    fun createComponent(): E
}
