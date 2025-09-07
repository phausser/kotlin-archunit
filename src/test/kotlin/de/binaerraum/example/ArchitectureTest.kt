package de.binaerraum.example

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val classes = ClassFileImporter().importPackages("de.binaerraum")

    @Test
    fun `controller should only depend on service`() {
        val rule: ArchRule = classes()
            .that().resideInAPackage("de.binaerraum.example.controller")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "de.binaerraum.example.service",
                "de.binaerraum.example.model",
                "java..",
                "kotlin..",
                "org.jetbrains.."
            )

        rule.check(classes)
    }

    @Test
    fun `service should only depend on repository and model`() {
        val rule: ArchRule = classes()
            .that().resideInAPackage("de.binaerraum.example.service")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "de.binaerraum.example.repository",
                "de.binaerraum.example.model",
                "java..",
                "kotlin..",
                "org.jetbrains.."
            )

        rule.check(classes)
    }

    @Test
    fun `repository should not depend on service or controller`() {
        val rule: ArchRule = classes()
            .that().resideInAPackage("de.binaerraum.example.repository")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "de.binaerraum.example.model",
                "java..",
                "kotlin..",
                "org.jetbrains.."
            )

        rule.check(classes)
    }

    @Test
    fun `no classes should depend on controller`() {
        val rule: ArchRule = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage("de.binaerraum.example.controller")

        rule.check(classes)
    }
}
