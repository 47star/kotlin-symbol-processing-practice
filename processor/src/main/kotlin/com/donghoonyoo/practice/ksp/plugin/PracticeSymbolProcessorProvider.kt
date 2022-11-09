package com.donghoonyoo.practice.ksp.plugin

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
@Suppress("unused")
class PracticeSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        SubclassCollectionGenerationSymbolProcessor(environment.codeGenerator)
}
