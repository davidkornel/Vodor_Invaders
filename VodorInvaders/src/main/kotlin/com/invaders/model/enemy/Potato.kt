package main.kotlin.com.invaders.model.enemy

import javafx.scene.image.Image
import main.kotlin.com.invaders.getResource

class Potato(var posX: Double, var posY: Double) {

    var image: Image = Image(getResource("/potato_90x90.png"))

    companion object {
        const val SIZE = 90
    }

    fun updatePosition(x: Double, y: Double) {
        posX += x
        posY += y
    }
}