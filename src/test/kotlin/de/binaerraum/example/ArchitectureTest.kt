package de.binaerraum.example

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import de.binaerraum.example.annotation.Service
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val classes = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages("de.binaerraum.example")

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
                "de.binaerraum.example.annotation",
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
    fun `model classes should not depend on other layers`() {
        val rule: ArchRule = classes()
            .that().resideInAPackage("de.binaerraum.example.model")
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

    @Test
    fun `controller classes should be named with Controller suffix`() {
        val rule: ArchRule = classes()
            .that().resideInAnyPackage("de.binaerraum.example.controller")
            .should().haveSimpleNameEndingWith("Controller")

        rule.check(classes)
    }

    @Test
    fun `controllers should not access repositories directly`() {
        val rule: ArchRule = noClasses()
            .that().resideInAPackage("de.binaerraum.example.controller")
            .should().dependOnClassesThat()
            .resideInAPackage("de.binaerraum.example.repository")

        rule.check(classes)
    }

    @Test
    fun `repository public methods should start with find`() {
        val rule: ArchRule = methods()
            .that().areDeclaredInClassesThat().resideInAPackage("de.binaerraum.example.repository")
            .and().arePublic()
            .should().haveNameStartingWith("find")

        rule.check(classes)
    }

    @Test
    fun `services should not use System out`() {
        val rule: ArchRule = noClasses()
            .that().resideInAPackage("de.binaerraum.example.service")
            .should().accessClassesThat().areAssignableTo(System::class.java)

        rule.check(classes)
    }

    @Test
    fun `classes annotated with Service should reside in service package`() {
        val rule: ArchRule = classes()
            .that().areAnnotatedWith(Service::class.java)
            .should().resideInAPackage("de.binaerraum.example.service")

        rule.check(classes)
    }

    @Test
    fun `classes should have at most 10 methods`() {
        val haveAtMostTenMethods: ArchCondition<JavaClass> =
            object : ArchCondition<JavaClass>("have at most 10 methods") {
                override fun check(item: JavaClass, events: ConditionEvents) {
                    val methodCount = item.methods.size
                    if (methodCount > 10) {
                        val message = "${item.name} has $methodCount methods, which exceeds the limit of 10"
                        events.add(SimpleConditionEvent.violated(item, message))
                    }
                }
            }

        val rule: ArchRule = classes()
            .that().resideInAPackage("de.binaerraum.example..")
            .should(haveAtMostTenMethods)

        rule.check(classes)
    }
}
