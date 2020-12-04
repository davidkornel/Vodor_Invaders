package main.kotlin.com.invaders.controller

import main.kotlin.com.invaders.getChance
import main.kotlin.com.invaders.model.droploot.Beer
import main.kotlin.com.invaders.model.droploot.Drumstick
import main.kotlin.com.invaders.model.enemy.Potato

class LootController {

    val beerList = mutableListOf<Beer>()
    val drumstickList = mutableListOf<Drumstick>()
    private var lastMoveTime: Long = 0

    fun spawnLoot(potatoes: MutableList<Potato>) {
        for (p: Potato in potatoes) {
            if (getChance(10)) {
                beerList.add(Beer(p.posX, p.posY))
            } else if (getChance(20)) {
                drumstickList.add(Drumstick(p.posX, p.posY))
            }
        }
    }

    fun updateLoot(elapsedNano: Long) {
        lastMoveTime += elapsedNano
        if (lastMoveTime >= 30_000_000) {
            if (beerList.isNotEmpty()) {
                for (b: Beer in beerList) {
                    b.updatePosition(0.0, 4.0)
                }
            }
            if (drumstickList.isNotEmpty()) {
                for (ds: Drumstick in drumstickList) {
                    ds.updatePosition(0.0, 4.0)
                }
            }
            lastMoveTime = 0
        }
    }

    fun newGame() {
        beerList.clear()
        drumstickList.clear()
    }
}