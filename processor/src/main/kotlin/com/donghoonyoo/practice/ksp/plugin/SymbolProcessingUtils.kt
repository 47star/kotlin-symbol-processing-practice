package com.donghoonyoo.practice.ksp.plugin

import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName

fun KSDeclaration.toClassName() = ClassName(packageName.asString(), simpleName.asString())
