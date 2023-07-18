package com.riza.example.common

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * release builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds.
 */
@com.riza.example.common.OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting
