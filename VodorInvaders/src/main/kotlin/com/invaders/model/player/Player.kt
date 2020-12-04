package main.kotlin.com.invaders.model.player

import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import main.kotlin.com.invaders.Game
import main.kotlin.com.invaders.getResource

class Player {


    internal var playerX = Game.WIDTH / 2
    internal var playerY = Game.HEIGHT / 2

    internal var image = Image(getResource("/peeler_lvl1.png"))
    var lvlOfWeapon = 1
    var healthPoints = 3

    internal var beams = mutableListOf<LaserBeam>()
    private var timeSinceLastBlast: Long = 0

    private var moveCnt = 0

    companion object {
        const val SIZE_WIDTH = 80
        const val SIZE_HEIGHT = 190
    }

    private fun checkCollisionWithWalls(keys: MutableSet<KeyCode>) {
        //coordinates counted from the top left corner

        //bottom wall collision check
        if (playerY + image.height >= Game.HEIGHT) {
            keys.remove(KeyCode.DOWN)
        }
        //left wall collision check
        if (playerX <= 0) {
            keys.remove(KeyCode.LEFT)
        }
        //top wall collision check
        if (playerY <= 0) {
            keys.remove(KeyCode.UP)
        }
        //right wall collision check
        if (playerX + image.width >= Game.WIDTH) {
            keys.remove(KeyCode.RIGHT)
        }


    }

    fun updatePlayerPosition(keys: MutableSet<KeyCode>) {
        checkCollisionWithWalls(keys)
        //limited player move speed
        if (moveCnt % 3 == 0) {
            if (keys.contains(KeyCode.LEFT)) {
                playerX--
            }
            if (keys.contains(KeyCode.RIGHT)) {
                playerX++
            }
            if (keys.contains(KeyCode.UP)) {
                playerY--
            }
            if (keys.contains(KeyCode.DOWN)) {
                playerY++
            }
        }
        moveCnt++
    }

    fun updatePlayerShoot(keys: MutableSet<KeyCode>, elapsedNanos: Long) {

        //nanosecond equal to one billionth of a second
        //remember that
        timeSinceLastBlast += elapsedNanos
        if (keys.contains(KeyCode.SPACE)) {
            when (lvlOfWeapon) {
                //level one blaster is shooting once a second
                1 -> if (timeSinceLastBlast < 800_000_000) {
                    timeSinceLastBlast += elapsedNanos
                } else {
                    timeSinceLastBlast = 0
                    beams.add(LaserBeam(this))
                }
                2 -> if (timeSinceLastBlast < 600_000_000) {
                    timeSinceLastBlast += elapsedNanos
                } else {
                    timeSinceLastBlast = 0
                    beams.add(LaserBeam(this))
                }
                3 -> if (timeSinceLastBlast < 500_000_000) {
                    timeSinceLastBlast += elapsedNanos
                } else {
                    timeSinceLastBlast = 0
                    beams.add(LaserBeam(this))
                }
                4 -> if (timeSinceLastBlast < 400_000_000) {
                    timeSinceLastBlast += elapsedNanos
                } else {
                    timeSinceLastBlast = 0
                    beams.add(LaserBeam(this))
                }
                5 -> if (timeSinceLastBlast < 300_000_000) {
                    timeSinceLastBlast += elapsedNanos
                } else {
                    timeSinceLastBlast = 0
                    beams.add(LaserBeam(this))
                }
                else -> println("BLAST WHEN ELSE ERROR")
            }
        }
        checkRemovable(emptyList())

    }

    private fun checkRemovable(toRemoveHit: List<LaserBeam>) {
        val toRemove = mutableListOf<LaserBeam>()
        toRemove.addAll(toRemoveHit)
        for (b: LaserBeam in beams) {
            if (!b.isOnScreen) {
                toRemove.add(b)
            }
        }
        if (toRemove.isNotEmpty()) {
            beams.removeAll(toRemove)

        }
    }

    fun updatePlayerLaserBeamPosition() {
        for (b: LaserBeam in beams) {
            b.updateLaserBeamPosition()
        }
    }

    fun newGame() {
        playerX = Game.WIDTH / 2
        playerY = Game.HEIGHT / 2
        healthPoints = 3
        beams.clear()
        lvlOfWeapon = 1
    }

}