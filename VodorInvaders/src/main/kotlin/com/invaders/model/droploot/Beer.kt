package main.kotlin.com.invaders.model.droploot

import javafx.scene.image.Image
import main.kotlin.com.invaders.getResource

class Beer(var posX: Double, var posY: Double) {

    var image: Image = Image(getResource("/beer_50x40.png"))

    companion object {
        const val SIZE_WIDTH = 50
        const val SIZE_HEIGHT = 40
    }

    fun updatePosition(x: Double, y: Double) {
        posX += x
        posY += y
    }
}