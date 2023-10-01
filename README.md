# ide-plugin-code-explorer-panel

![Build](https://github.com/lizaerem/ide-plugin-code-explorer-panel/workflows/Build/badge.svg)
![](https://img.shields.io/badge/Kotlin-orange)
[![GitHub release (with filter)](https://img.shields.io/github/v/release/lizaerem/ide-plugin-code-explorer-panel)](https://github.com/lizaerem/ide-plugin-code-explorer-panel/releases/tag/v0.1.0-alpha)
##
<!-- Plugin description -->
A simple plugin for the IDEA platform that adds an additional side panel displaying the number of classes and functions for all `Kotlin`/`Java` files in the currently open project, as well as for currently open file:

<img width="487" alt="image" src="https://github.com/lizaerem/ide-plugin-code-explorer-panel/assets/70374721/58a491e7-4f07-499d-bcfa-fbb01d9066ff">

##
**Ideas to implement**: make it possible to choose from the list of files to count on a particular subset of files; include name of currently open file.
<!-- Plugin description end -->

## Implemented
- Core logic in [MyProjectService.kt](https://github.com/lizaerem/ide-plugin-code-explorer-panel/blob/main/src/main/kotlin/com/github/lizaerem/ideplugincodeexplorerpanel/services/MyProjectService.kt)
  * collecting stats for the whole project (both `Kotlin` and `Java` files separately)
  * collecting stats for currently open file
- Unit tests in [MyPluginTest.kt](https://github.com/lizaerem/ide-plugin-code-explorer-panel/blob/main/src/test/kotlin/com/github/lizaerem/ideplugincodeexplorerpanel/MyPluginTest.kt)
- UI in [CodeExplorerWindowFactory.kt](https://github.com/lizaerem/ide-plugin-code-explorer-panel/blob/main/src/main/kotlin/com/github/lizaerem/ideplugincodeexplorerpanel/toolWindow/CodeExplorerWindowFactory.kt)
  * scrollable window
  * visually readable data
  * `Refresh` and `Hide` buttons
- [Dependencies](https://github.com/lizaerem/ide-plugin-code-explorer-panel/blob/main/src/main/resources/META-INF/plugin.xml)/[Settings](https://github.com/lizaerem/ide-plugin-code-explorer-panel/blob/main/build.gradle.kts)

## Installation

- _Manually_:

  Download the [latest release](https://github.com/lizaerem/ide-plugin-code-explorer-panel/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> > Choose <kbd>ide-plugin-code-explorer-panel-0.0.1.jar</kbd> 

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
