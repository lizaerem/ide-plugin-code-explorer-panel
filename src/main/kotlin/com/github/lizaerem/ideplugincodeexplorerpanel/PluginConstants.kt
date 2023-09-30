package com.github.lizaerem.ideplugincodeexplorerpanel

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction

object PluginConstants {
    val KOTLIN_CLASS = KtClass::class.java
    val JAVA_CLASS = PsiClass::class.java
    val KOTLIN_METHOD = KtNamedFunction::class.java
    val JAVA_METHOD = PsiMethod::class.java
}
