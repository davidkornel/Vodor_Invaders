package main.kotlin.com.invaders.controller.button

import javafx.event.EventHandler
import javafx.scene.control.Button

class UpgradeButton : Button() {
    var upgrade = false

    init {
        this.text = "Upgrade weapon"
        this.isFocusTraversable = false
        this.layoutX = 100.0
        this.layoutY = 990.0
        this.onAction = EventHandler {
            upgrade = true
        }
    }

    fun disable() {
        this.text = "Weapon maxed out"
        this.isDisable = true
    }

    fun enable() {
        this.text = "Upgrade Weapon"
        this.isDisable = false
    }
}
