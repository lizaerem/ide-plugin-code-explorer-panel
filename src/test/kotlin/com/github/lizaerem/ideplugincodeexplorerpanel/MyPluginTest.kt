package com.github.lizaerem.ideplugincodeexplorerpanel

import com.github.lizaerem.ideplugincodeexplorerpanel.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFile
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    fun testCountsInKotlinFile() {
        val projectService = project.service<MyProjectService>()

        val psiFile: PsiFile? = myFixture.configureByFile("DummyClass.kt")

        assertNotNull(psiFile)
        assertEquals(1, psiFile?.let { projectService.countClassesInFile(it, PluginConstants.KOTLIN_CLASS) })
        assertEquals(2, psiFile?.let { projectService.countMethodsInKotlinFile(it) })
    }

    fun testCountsInOneFileJavaProject() {
        val projectService = project.service<MyProjectService>()
        myFixture.configureByFile("DummyClass.java")

        assertEquals(Pair(1, 2), projectService.countJavaClassesAndFunctions(project))
    }

    fun testCountsInUnsupportedFile() {
        val projectService = project.service<MyProjectService>()
        myFixture.configureByFile("UnsupportedExtension.txt")

        assertEquals(Pair(0, 0), projectService.getStatsForOpenFile(project))
    }

    fun testCountsInComplexProject() {
        val projectService = project.service<MyProjectService>()
        myFixture.configureByFile("myComplexProject/src/main/kotlin/com/example/Main.kt")
        myFixture.configureByFile("myComplexProject/src/main/java/com/example/MyClass.java")
        myFixture.configureByFile("myComplexProject/src/main/java/com/example/MyInterface.java")
        myFixture.configureByFile("myComplexProject/src/main/java/com/example/subpackage/SubClass.java")
        myFixture.configureByFile("myComplexProject/src/test/kotlin/com/example/MyTest.kt")

        assertEquals(Pair(2, 2), projectService.countKotlinClassesAndFunctions(project))
        assertEquals(Pair(3, 3), projectService.countJavaClassesAndFunctions(project))
    }

    override fun getTestDataPath() = "src/test/testData/resources"
}
