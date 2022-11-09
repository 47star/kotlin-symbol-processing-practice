plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(project(":annotations"))
    ksp(project(":processor"))
}

sourceSets.main.kotlin.srcDir("build/generated/ksp/main/kotlin")
