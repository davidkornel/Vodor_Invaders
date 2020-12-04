package main.kotlin.com.invaders.controller

import main.kotlin.com.invaders.Game
import main.kotlin.com.invaders.getChance
import main.kotlin.com.invaders.model.enemy.Potato
import main.kotlin.com.invaders.model.enemy.PotatoLaser

class PotatoGridController(private val widthOfGrid: Int) {

    internal var firstPotatoGridWave = mutableListOf<Potato>()
    var nextPotatoGridWave = mutableListOf<Potato>()
    val laser = mutableListOf<PotatoLaser>()
    private val heightOfGrid = widthOfGrid / 2
    private var bottomPositionOfFirstWave = 200
    private var topPositionOfFirstWave = 0
    private var bottomPositionOfNextWave = 0
    private var bottomPositionToStop = 0
    private var lastMoveTime: Long = 0
    private var lastShootTime: Long = 0
    private var directionChangeCounterForFirstWave = 0
    private var directionChangeCounterForNextWave = 0
    //ways 1:down and right 2: down and left 3:right 4:left
    private var wayOfFirstWave: Int = 1
    private var wayOfNextWave: Int = 1

    init {
        newGame(widthOfGrid)
    }

    internal fun controlFirstWave(elapsedNano: Long) {

        lastMoveTime += elapsedNano

        if (lastMoveTime >= 50_000_000) {
            //moving down and sideways
            if (bottomPositionOfFirstWave != bottomPositionToStop) {
                for (p in firstPotatoGridWave) {
                    if (this.wayOfFirstWave == 1) {
                        p.updatePosition(5.0, 2.0)
                    }
                    if (this.wayOfFirstWave == 2) {
                        p.updatePosition(-5.0, 2.0)

                    }
                }
                directionChangeCounterForFirstWave++
                if (directionChangeCounterForFirstWave % 3 == 0) {
                    this.wayOfFirstWave = if (this.wayOfFirstWave == 1) {
                        2
                    } else {
                        1
                    }
                }
                bottomPositionOfFirstWave += 2
                topPositionOfFirstWave += 2
                println(topPositionOfFirstWave)
                if (bottomPositionOfFirstWave == bottomPositionToStop) {
                    wayOfFirstWave = 3
                    directionChangeCounterForFirstWave = 0
                }
            } else {
                //moving only sideways
                for (p in firstPotatoGridWave) {
                    if (this.wayOfFirstWave == 3) {
                        p.updatePosition(5.0, 0.0)
                    }
                    if (this.wayOfFirstWave == 4) {
                        p.updatePosition(-5.0, 0.0)
                    }
                }
                directionChangeCounterForFirstWave++
                if (directionChangeCounterForFirstWave % 3 == 0) {
                    this.wayOfFirstWave = if (this.wayOfFirstWave == 3) {
                        4
                    } else {
                        3
                    }
                }
            }
            lastMoveTime = 0
            potatoLaserMove()
            if (firstPotatoGridWave.isEmpty()) {
                changeWaves()
            }
            if (firstPotatoGridWave.size <= 20 && nextPotatoGridWave.isEmpty() && topPositionOfFirstWave > 1) {
                spawnNextPotatoGridWave()
            }
            controlNextWave()
        }

        lastShootTime += elapsedNano
        if (lastShootTime >= 2_000_000_000) {
            potatoLaserAdd(firstPotatoGridWave)
            potatoLaserAdd(nextPotatoGridWave)
            lastShootTime = 0
        }
    }

    private fun controlNextWave() {
        if (nextPotatoGridWave.isNotEmpty()) {
            //moving down and sideways
            if (bottomPositionOfNextWave != topPositionOfFirstWave) {
                for (p in nextPotatoGridWave) {
                    if (this.wayOfNextWave == 1) {
                        p.updatePosition(5.0, 2.0)
                    }
                    if (this.wayOfNextWave == 2) {
                        p.updatePosition(-5.0, 2.0)

                    }
                }
                directionChangeCounterForNextWave++
                if (directionChangeCounterForNextWave % 3 == 0) {
                    this.wayOfNextWave = if (this.wayOfNextWave == 1) {
                        2
                    } else {
                        1
                    }
                }
                bottomPositionOfNextWave += 2
                if (bottomPositionOfNextWave == topPositionOfFirstWave) {
                    wayOfNextWave = 3
                    directionChangeCounterForNextWave = 0
                }
            } else {
                //moving only sideways
                for (p in nextPotatoGridWave) {
                    if (this.wayOfNextWave == 3) {
                        p.updatePosition(5.0, 0.0)
                    }
                    if (this.wayOfNextWave == 4) {
                        p.updatePosition(-5.0, 0.0)
                    }
                }
                directionChangeCounterForNextWave++
                if (directionChangeCounterForNextWave % 3 == 0) {
                    this.wayOfNextWave = if (this.wayOfNextWave == 3) {
                        4
                    } else {
                        3
                    }
                }
            }
        }
    }

    private fun changeWaves() {
        bottomPositionOfFirstWave = bottomPositionOfNextWave
        topPositionOfFirstWave = bottomPositionOfFirstWave - heightOfGrid * Potato.SIZE
        firstPotatoGridWave = nextPotatoGridWave
        nextPotatoGridWave = mutableListOf()
        println("top: $topPositionOfFirstWave")
    }

    private fun potatoLaserMove() {
        for (potatoLaser in laser) {
            potatoLaser.updatePosition()
        }
        checkRemovable()
    }

    private fun potatoLaserAdd(potatoGridWave: MutableList<Potato>) {
        for (potato in potatoGridWave) {
            if (getChance(10) && laser.size <= 10) {
                laser.add(PotatoLaser(potato))
            }
        }
    }

    fun checkRemovable() {
        val toRemove = mutableListOf<PotatoLaser>()
        for (l in laser) {
            if (!l.isOnScreen) {
                toRemove.add(l)
            }
        }
        if (toRemove.isNotEmpty()) {
            laser.removeAll(toRemove)

        }
    }

    fun newGame(widthOfGrid: Int) {
        //clearing data
        firstPotatoGridWave.clear()
        nextPotatoGridWave.clear()
        laser.clear()
        bottomPositionOfFirstWave = 200
        bottomPositionToStop = 0
        val startPositionX = if (widthOfGrid % 2 == 0) {
            Game.WIDTH / 2 - (widthOfGrid / 2) * Potato.SIZE
        } else {
            Game.WIDTH / 2 - (widthOfGrid / 2) * Potato.SIZE - Potato.SIZE / 2
        }

        for (i: Int in 0 until heightOfGrid) {
            val y = i * Potato.SIZE
            for (j: Int in 0 until widthOfGrid) {
                firstPotatoGridWave.add(Potato((j * Potato.SIZE + startPositionX).toDouble(), bottomPositionOfFirstWave - y.toDouble()))
            }
        }
        bottomPositionToStop = heightOfGrid * Potato.SIZE + 200
        topPositionOfFirstWave = bottomPositionOfFirstWave - heightOfGrid * Potato.SIZE
    }

    private fun spawnNextPotatoGridWave() {
        val startPositionX = if (widthOfGrid % 2 == 0) {
            Game.WIDTH / 2 - (widthOfGrid / 2) * Potato.SIZE
        } else {
            Game.WIDTH / 2 - (widthOfGrid / 2) * Potato.SIZE - Potato.SIZE / 2
        }
        for (i: Int in 0 until heightOfGrid) {
            val y = i * Potato.SIZE
            for (j: Int in 0 until widthOfGrid) {
                nextPotatoGridWave.add(Potato((j * Potato.SIZE + startPositionX).toDouble(), -90 - y.toDouble()))
            }
        }
        bottomPositionOfNextWave = -90
    }

}