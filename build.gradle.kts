buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        val kotlinVersion = "1.7.20"
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:$kotlinVersion-1.0.8")
    }
}

allprojects {
    group = "com.donghoonyoo.practice.ksp"
    version = "0.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
