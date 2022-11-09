package com.donghoonyoo.practice.ksp.application

import com.donghoonyoo.practice.ksp.annotations.EnableCollectSubclass

@EnableCollectSubclass
abstract class Parent {
    companion object
}

class ChildA : Parent()
class ChildB : Parent()
class ChildC : Parent()
