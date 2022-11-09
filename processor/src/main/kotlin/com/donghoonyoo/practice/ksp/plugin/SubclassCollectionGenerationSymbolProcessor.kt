package com.donghoonyoo.practice.ksp.plugin

import com.donghoonyoo.practice.ksp.annotations.EnableCollectSubclass
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.OutputStreamWriter
import kotlin.reflect.KClass

class SubclassCollectionGenerationSymbolProcessor(
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val parents = resolver.getSymbolsWithAnnotation(EnableCollectSubclass::class.java.canonicalName)

        resolver.getAllFiles()
            .flatMap { it.declarations }
            .groupBy { ksDeclaration ->
                val superTypes = ksDeclaration.closestClassDeclaration()?.superTypes ?: emptySequence()
                parents.find { superTypes.toList().any { st -> st.toString() == it.toString() } }
            }
            .onEach { (k, v) ->
                if (k == null)
                    return@onEach
                k.accept(SubclassCollectionGenerationVisitor(codeGenerator, k as KSDeclaration, v), Unit)
            }

        return emptyList()
    }

    class SubclassCollectionGenerationVisitor(
        private val codeGenerator: CodeGenerator,
        private val parentDeclaration: KSDeclaration,
        private val subClasses: List<KSDeclaration>,
    ) : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val fileSpec = FileSpec.builder(
                classDeclaration.packageName.asString(),
                "${classDeclaration.simpleName.asString()}_SubclassCollection",
            )
                .addProperty(
                    PropertySpec.builder(
                        "subclasses",
                        List::class.asClassName().parameterizedBy(
                            KClass::class.asClassName().parameterizedBy(
                                WildcardTypeName.producerOf(parentDeclaration.toClassName())
                            )
                        ),
                        KModifier.PUBLIC,
                    )
                        .receiver(parentDeclaration.toClassName().nestedClass("Companion"))
                        .delegate(buildCodeBlock {
                            add(
                                "lazy {\nlistOf(\n${"%L,\n".repeat(subClasses.size)})\n}",
                                *subClasses.map { ksDeclaration ->
                                    "${ksDeclaration.toClassName().canonicalName}::class"
                                }.toTypedArray()
                            )
                        })
                        .build()
                )
                .build()

            OutputStreamWriter(
                codeGenerator.createNewFile(
                    Dependencies(
                        false,
                        parentDeclaration.containingFile!!,
                    ),
                    fileSpec.packageName,
                    fileSpec.name,
                ),
                "UTF-8"
            ).use { fileSpec.writeTo(it) }
        }
    }
}
