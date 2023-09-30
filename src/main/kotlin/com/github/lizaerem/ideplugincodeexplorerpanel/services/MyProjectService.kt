package com.github.lizaerem.ideplugincodeexplorerpanel.services

import com.github.lizaerem.ideplugincodeexplorerpanel.MyBundle
import com.github.lizaerem.ideplugincodeexplorerpanel.PluginConstants
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.psi.KtNamedFunction

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    private val psiManager = PsiManager.getInstance(project)

    fun countKotlinClassesAndFunctions(project: Project): Pair<Int, Int> {
        val kotlinFiles = FilenameIndex.getAllFilesByExt(project, "kt", GlobalSearchScope.projectScope(project))

        if (thisLogger().isDebugEnabled) {
            thisLogger().debug("Kotlin files number: $kotlinFiles")
        }

        val psiFiles = kotlinFiles.mapNotNull { file -> psiManager.findFile(file) }

        return Pair(psiFiles.sumOf { file -> countClassesInFile(file, PluginConstants.KOTLIN_CLASS) },
            psiFiles.sumOf { file -> countMethodsInKotlinFile(file) })
    }

    fun countJavaClassesAndFunctions(project: Project): Pair<Int, Int> {
        val javaFiles = FilenameIndex.getAllFilesByExt(project, "java", GlobalSearchScope.projectScope(project))

        if (thisLogger().isDebugEnabled) {
            thisLogger().debug("Java files number: $javaFiles")
        }

        val psiFiles = javaFiles.mapNotNull { file -> psiManager.findFile(file) }

        return Pair(psiFiles.sumOf { file -> countClassesInFile(file, PluginConstants.JAVA_CLASS) },
            psiFiles.sumOf { file -> countMethodsInJavaFile(file) })
    }

    fun countClassesInFile(psiFile: PsiFile, classType: Class<*>): Int {
        if (thisLogger().isDebugEnabled) {
            thisLogger().debug("(countClasses) PsiFile: $psiFile")
            psiFile.children.forEach { child ->
                thisLogger().debug("(countClasses) Child Class: ${child::class.java}")
            }
        }

        val classes = psiFile.children.filterIsInstance(classType).size
        return classes
    }

    // TODO: create one function with methodClass parameter (so it's not failing) for counting methods
    fun countMethodsInKotlinFile(psiFile: PsiFile): Int {
        if (thisLogger().isDebugEnabled) {
            thisLogger().debug("(countMethods) PsiFile: $psiFile")
        }

        val methods = PsiTreeUtil.findChildrenOfType(psiFile, KtNamedFunction::class.java)
        return methods.size
    }

    fun countMethodsInJavaFile(psiFile: PsiFile): Int {
        if (thisLogger().isDebugEnabled) {
            thisLogger().debug("(countMethods) PsiFile: $psiFile")
        }

        val methods = PsiTreeUtil.findChildrenOfType(psiFile, PsiMethod::class.java)
        return methods.size
    }

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }
}
