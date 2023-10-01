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
import com.intellij.icons.AllIcons
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.*
import javax.swing.*


class CodeExplorerWindowFactory : ToolWindowFactory {

    init {
        thisLogger().debug("Initialization of CodeExplorerWindowFactory")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val codeExplorerWindow = CodeExplorerWindow(toolWindow)
        val content =
            ContentFactory.getInstance().createContent(codeExplorerWindow.getContent(project, toolWindow), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class CodeExplorerWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()

        private val kotlinClassesLabel = JBLabel("Kotlin Classes:")
        private val kotlinMethodsLabel = JBLabel("Kotlin Methods:")
        private val javaClassesLabel = JBLabel("Java Classes:")
        private val javaMethodsLabel = JBLabel("Java Methods:")
        private val totalClassesLabel = JBLabel("Total Classes:")
        private val totalMethodsLabel = JBLabel("Total Methods:")
        private val openFileClassesLabel = JBLabel("Classes in currently open file:")
        private val openFileMethodsLabel = JBLabel("Methods in currently open file:")

        private val kotlinClassesValue = JBLabel()
        private val kotlinMethodsValue = JBLabel()
        private val javaClassesValue = JBLabel()
        private val javaMethodsValue = JBLabel()
        private val totalClassesValue = JBLabel()
        private val totalMethodsValue = JBLabel()
        private val openFileClassesValue = JBLabel()
        private val openFileMethodsValue = JBLabel()

        fun getContent(project: Project, toolWindow: ToolWindow) = JBScrollPane(
            JBPanel<JBPanel<*>>().apply {
                layout = BorderLayout()

                add(createTopPanel(), BorderLayout.NORTH)
                add(createCentralPanel(project), BorderLayout.CENTER)
                add(createButtonPanel(toolWindow, project), BorderLayout.SOUTH)

                border = JBUI.Borders.empty(10)
            }
        )

        private fun createTopPanel(): JBPanel<JBPanel<*>> {
            val topPanel = JBPanel<JBPanel<*>>()
            topPanel.layout = GridLayout(1, 2)

            val kotlinIcon = JBLabel(AllIcons.FileTypes.Unknown) // Why there is no Kotlin?...
            val javaIcon = JBLabel(AllIcons.FileTypes.Java)
            topPanel.add(kotlinIcon)
            topPanel.add(javaIcon)

            return topPanel
        }

        private fun createCentralPanel(project: Project): JPanel {
            val centerPanel = JPanel(GridBagLayout())

            val constraints = GridBagConstraints()
            constraints.fill = GridBagConstraints.BOTH
            constraints.insets = JBUI.insets(15, 30)

            updateStats(project)

            addComponent(centerPanel, constraints, kotlinClassesLabel, 0, 0)
            addComponent(centerPanel, constraints, javaClassesLabel, 2, 0)
            addComponent(centerPanel, constraints, kotlinClassesValue, 0, 1)
            addComponent(centerPanel, constraints, javaClassesValue, 2, 1)
            addComponent(centerPanel, constraints, kotlinMethodsLabel, 0, 2)
            addComponent(centerPanel, constraints, javaMethodsLabel, 2, 2)
            addComponent(centerPanel, constraints, kotlinMethodsValue, 0, 3)
            addComponent(centerPanel, constraints, javaMethodsValue, 2, 3)

            val separatorHorizontal = createLineDivider(JSeparator.HORIZONTAL, 1)
            constraints.gridwidth = 3
            addComponent(centerPanel, constraints, separatorHorizontal, 0, 4)

            addComponent(centerPanel, constraints, totalClassesLabel, 0, 5)
            addComponent(centerPanel, constraints, totalClassesValue, 2, 5)
            addComponent(centerPanel, constraints, totalMethodsLabel, 0, 6)
            addComponent(centerPanel, constraints, totalMethodsValue, 2, 6)

            val separatorHorizontal2 = createLineDivider(JSeparator.HORIZONTAL, 1)
            addComponent(centerPanel, constraints, separatorHorizontal2, 0, 7)

            addComponent(centerPanel, constraints, openFileClassesLabel, 0, 8)
            addComponent(centerPanel, constraints, openFileClassesValue, 2, 8)
            addComponent(centerPanel, constraints, openFileMethodsLabel, 0, 9)
            addComponent(centerPanel, constraints, openFileMethodsValue, 2, 9)

            val separatorVertical = createLineDivider(JSeparator.VERTICAL, 5)
            constraints.gridx = 1
            constraints.gridy = 0
            constraints.gridheight = 5
            constraints.gridwidth = 1
            addComponent(centerPanel, constraints, separatorVertical, 1, 0)

            return centerPanel
        }

        private fun createLineDivider(orientation: Int, gridHeight: Int): JSeparator {
            val separator = JSeparator(orientation)
            val separatorColor = UIManager.getColor("Separator.foreground")
            separator.foreground = separatorColor
            val separatorPreferredSize = separator.preferredSize
            separatorPreferredSize.height *= gridHeight
            separator.preferredSize = separatorPreferredSize

            return separator
        }

        private fun addComponent(
            container: JPanel,
            constraints: GridBagConstraints,
            component: Component,
            gridX: Int,
            gridY: Int
        ) {
            constraints.gridx = gridX
            constraints.gridy = gridY
            container.add(component, constraints)
        }

        private fun createButtonPanel(toolWindow: ToolWindow, project: Project): JPanel {
            val buttonPanel = JPanel(BorderLayout())
            val buttonsPanel = JPanel()
            buttonsPanel.layout = BoxLayout(buttonsPanel, BoxLayout.X_AXIS)

            val refreshButton = JButton("Refresh")
            refreshButton.toolTipText = "Update the statistics"
            refreshButton.isEnabled = true

            val updatingLabel = JBLabel("  Updating...")
            updatingLabel.isVisible = false

            refreshButton.addActionListener {
                refreshButton.isEnabled = false
                updatingLabel.isVisible = true

                updateStats(project)

                Timer(1000) {
                    updatingLabel.isVisible = false
                    refreshButton.isEnabled = true
                }.apply {
                    isRepeats = false
                    start()
                }
            }

            val hideButton = JButton("Hide")
            hideButton.addActionListener { toolWindow.hide(null) }

            buttonsPanel.add(refreshButton)
            buttonsPanel.add(Box.createHorizontalGlue())
            buttonsPanel.add(hideButton)

            buttonPanel.add(buttonsPanel, BorderLayout.WEST)
            buttonPanel.add(updatingLabel, BorderLayout.SOUTH)

            return buttonPanel
        }

        private fun updateStats(project: Project) {
            val (kotlinClasses, kotlinMethods) = service.countKotlinClassesAndFunctions(project)
            val (javaClasses, javaMethods) = service.countJavaClassesAndFunctions(project)
            val (openFileClasses, openFileMethods) = service.getStatsForOpenFile(project)

            kotlinClassesValue.text = "<html><b>$kotlinClasses</b></html>"
            kotlinMethodsValue.text = "<html><b>$kotlinMethods</b></html>"
            javaClassesValue.text = "<html><b>$javaClasses</b></html>"
            javaMethodsValue.text = "<html><b>$javaMethods</b></html>"
            totalClassesValue.text = "<html><b>${kotlinClasses + javaClasses}</b></html>"
            totalMethodsValue.text = "<html><b>${kotlinMethods + javaMethods}</b></html>"
            openFileClassesValue.text = "<html><b>$openFileClasses</b></html>"
            openFileMethodsValue.text = "<html><b>$openFileMethods</b></html>"
        }
    }
}
