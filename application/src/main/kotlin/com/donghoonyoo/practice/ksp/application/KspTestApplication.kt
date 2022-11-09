package com.donghoonyoo.practice.ksp.application

object KspTestApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        println(Parent.subclasses.map { it.java.canonicalName })
    }
}
