plugins {
    id("java")
    kotlin("jvm") version "1.8.10"
    id("antlr")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation(kotlin("test"))
    antlr("org.antlr:antlr4:4.9.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sourceSets.main {
    java.srcDirs("src/main/java/", "src/main/kotlin")
}

tasks.compileKotlin {
    dependsOn(":generateGrammarSource")
}
tasks.compileTestKotlin {
    dependsOn(":generateTestGrammarSource")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
}