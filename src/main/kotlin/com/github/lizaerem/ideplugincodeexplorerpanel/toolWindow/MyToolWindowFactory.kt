package com.github.lizaerem.ideplugincodeexplorerpanel.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.lizaerem.ideplugincodeexplorerpanel.services.MyProjectService


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().debug("Initialization of MyToolWindowFactory")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(project), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent(project: Project) = JBPanel<JBPanel<*>>().apply {
            val (kotlinClass, kotlinFunc) = service.countKotlinClassesAndFunctions(project)
            val (javaClass, javaFunc) = service.countJavaClassesAndFunctions(project)

            val kotlinStat = JBLabel("Kotlin Classes/Functions: ${kotlinClass}/${kotlinFunc}")
            val javaStat = JBLabel("Java Classes/Functions: ${javaClass}/${javaFunc}")
            val total = JBLabel("Total Classes/Functios: ${kotlinClass + javaClass}/${kotlinFunc + javaFunc}")

            add(kotlinStat)
            add(javaStat)
            add(total)
        }
    }
}
