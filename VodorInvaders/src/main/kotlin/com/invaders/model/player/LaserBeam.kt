package main.kotlin.com.invaders.model.player

import javafx.scene.image.Image
import main.kotlin.com.invaders.getResource

class LaserBeam(player: Player) {


    internal var posX: Double = player.playerX + (player.image.width / 3)
    internal var posY: Double = player.playerY.toDouble()

    internal var image: Image = Image(getResource("/laser_lvl1.png"))

    internal var isOnScreen = true

    internal fun updateLaserBeamPosition() {
        posY--
        if (posY + 50 == 0.toDouble()) {
            isOnScreen = false
        }
    }
}