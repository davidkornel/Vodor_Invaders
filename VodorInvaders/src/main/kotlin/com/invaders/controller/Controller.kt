package main.kotlin.com.invaders.controller

import javafx.scene.input.KeyCode
import main.kotlin.com.invaders.Game
import main.kotlin.com.invaders.controller.button.UpgradeButton
import main.kotlin.com.invaders.model.droploot.Beer
import main.kotlin.com.invaders.model.droploot.Drumstick
import main.kotlin.com.invaders.model.player.Player

class Controller(private var player: Player, private var potatoGridController: PotatoGridController, private var upgradeButton: UpgradeButton) {


    var score = 0
    var collectedBeers = 0
    var collectedDrumsticks = 0
    var isAlive = true

    private val hitController = HitController(player, potatoGridController)
    private val lootController = LootController()

    fun oneTick(currentlyActiveKeys: MutableSet<KeyCode>, elapsedNanos: Long) {

        // check if there is any hit
        hitController.checkPlayerHitPotatoWithLaser(lootController)
        hitController.checkPotatoHitPlayerWithLaser()
        checkIfPlayerDead()
        score = hitController.score
        // perform world updates
        player.updatePlayerPosition(currentlyActiveKeys)
        player.updatePlayerLaserBeamPosition()
        player.updatePlayerShoot(currentlyActiveKeys, elapsedNanos)
        potatoGridController.controlFirstWave(elapsedNanos)
        //lowering loots
        lootController.updateLoot(elapsedNanos)
        checkPlayerAndLootCollision()
        //check for weapon upgrade
        checkForUpgrade()
    }

    private fun checkForUpgrade() {
        if (upgradeButton.upgrade) {
            if (collectedBeers >= 2 && collectedDrumsticks >= 4) {
                player.lvlOfWeapon++
                collectedBeers -= 2
                collectedDrumsticks -= 4
                if (player.lvlOfWeapon == 5) {
                    upgradeButton.disable()
                }
            }
            upgradeButton.upgrade = false
        }
    }

    private fun checkIfPlayerDead() {
        if (player.healthPoints <= 0) {
            isAlive = false
        }
    }

    fun newGame() {
        potatoGridController.newGame(10)
        player.newGame()
        lootController.newGame()
        hitController.score = 0
        collectedDrumsticks = 0
        collectedBeers = 0
        isAlive = true
        upgradeButton.enable()
        println("New game successfully initialized")
    }

    private fun checkPlayerAndLootCollision() {
        val toRemoveBeerList = mutableListOf<Beer>()
        val toRemoveDrumstickList = mutableListOf<Drumstick>()

        if (lootController.drumstickList.isNotEmpty()) {
            for (ds: Drumstick in lootController.drumstickList) {
                if (player.playerX + Player.SIZE_WIDTH / 2 <= ds.posX + Drumstick.SIZE_WIDTH &&
                        player.playerX + Player.SIZE_WIDTH / 2 >= ds.posX &&
                        player.playerY + Player.SIZE_HEIGHT / 2 >= ds.posY &&
                        player.playerY + Player.SIZE_HEIGHT / 2 <= ds.posY + Drumstick.SIZE_HEIGHT) {
                    collectedDrumsticks++
                    toRemoveDrumstickList.add(ds)
                }
                if (ds.posY > Game.HEIGHT) {
                    toRemoveDrumstickList.add(ds)
                }
            }
            lootController.drumstickList.removeAll(toRemoveDrumstickList)
        }
        if (lootController.beerList.isNotEmpty()) {
            for (b: Beer in lootController.beerList) {
                if (player.playerX + Player.SIZE_WIDTH / 2 <= b.posX + Beer.SIZE_WIDTH &&
                        player.playerX + Player.SIZE_WIDTH / 2 >= b.posX &&
                        player.playerY + Player.SIZE_HEIGHT / 2 >= b.posY &&
                        player.playerY + Player.SIZE_HEIGHT / 2 <= b.posY + Beer.SIZE_HEIGHT) {
                    collectedBeers++
                    toRemoveBeerList.add(b)
                }
                if (b.posY > Game.HEIGHT) {
                    toRemoveBeerList.add(b)
                }
            }
            lootController.beerList.removeAll(toRemoveBeerList)
        }
    }


    fun getBeers(): MutableList<Beer> {
        return lootController.beerList
    }

    fun getDrumstick(): MutableList<Drumstick> {
        return lootController.drumstickList
    }
}