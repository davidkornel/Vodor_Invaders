package main.kotlin.com.invaders.controller

import main.kotlin.com.invaders.model.enemy.Potato
import main.kotlin.com.invaders.model.player.LaserBeam
import main.kotlin.com.invaders.model.player.Player

class HitController(private var player: Player, private var potatoGridController: PotatoGridController) {

    var score = 0

    fun checkPlayerHitPotatoWithLaser(lootController: LootController) {
        val toRemovePotatoes = mutableListOf<Potato>()
        val toRemoveBeams = mutableListOf<LaserBeam>()

        //hitbox of the potato
        //potato is 50X50
        //laser beam is 50X17
        //8 pixels from every side of potato picture

        //8 pixels from both side of laser picture
        //5 pixels form the top of laser
        val allPotato = mutableListOf<Potato>()
        allPotato.addAll(potatoGridController.firstPotatoGridWave)
        allPotato.addAll(potatoGridController.nextPotatoGridWave)

        for (b: LaserBeam in player.beams) {
            for (p: Potato in allPotato) {
                if (b.posY > 1)
                    if (b.posY + 5 <= p.posY + Potato.SIZE * 8 / 9 &&
                            b.posX + 8 >= p.posX + Potato.SIZE / 9 &&
                            b.posX + 11 <= p.posX + Potato.SIZE * 8 / 9 &&
                            b.posY > p.posY) {
                        toRemoveBeams.add(b)
                        toRemovePotatoes.add(p)
                        score++
                    }
            }
        }
        if (toRemovePotatoes.isNotEmpty()) {
            lootController.spawnLoot(toRemovePotatoes)
        }
        player.beams.removeAll(toRemoveBeams)
        potatoGridController.firstPotatoGridWave.removeAll(toRemovePotatoes)
        potatoGridController.nextPotatoGridWave.removeAll(toRemovePotatoes)
    }

    fun checkPotatoHitPlayerWithLaser() {
        if (potatoGridController.laser.isNotEmpty()) {
            for (laser in potatoGridController.laser) {
                if (laser.posX >= player.playerX + player.image.width / 6 &&
                        laser.posX <= player.playerX + player.image.width * 4 / 6 &&
                        laser.posY + laser.image.height >= player.playerY + player.image.height / 3 &&
                        laser.posY + laser.image.height <= player.playerY + player.image.height) {

                    player.healthPoints--
                    laser.isOnScreen = false
                }
            }
            potatoGridController.checkRemovable()
        }
    }
}