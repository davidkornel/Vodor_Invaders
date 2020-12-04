package main.kotlin.com.invaders.model.enemy

import javafx.scene.image.Image
import main.kotlin.com.invaders.Game
import main.kotlin.com.invaders.getResource

class PotatoLaser(potato: Potato) {

    internal var posX: Double = potato.posX + potato.image.width / 2
    internal var posY: Double = potato.posY + potato.image.height * 2 / 3

    internal var image: Image = Image(getResource("/potato_laser.png"))

    internal var isOnScreen = true


    internal fun updatePosition() {
        //use this to adjust speed of pot. laser
        posY += 5
        if (posY - 50 >= Game.HEIGHT.toDouble()) {
            isOnScreen = false
        }
    }
}