package main.kotlin.com.invaders.controller.button

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import main.kotlin.com.invaders.getResource


class PauseButton : Button() {

    var gamePaused = false
    private val image = getResource("")
    private val imageView = ImageView(image)

    init {
        this.text = "Pause"
        this.graphic = imageView
        this.isFocusTraversable = false
        this.layoutX = 0.0
        this.layoutY = 990.0
        this.onAction = EventHandler {
            gamePaused = !gamePaused
            if (gamePaused) {
                this.text = "Continue"
            } else {
                this.text = "Pause"
            }
        }
    }
}

