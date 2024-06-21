package dev.pan.sunrain.presentation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Splash : Screen()

    @Serializable
    object Home : Screen()
}