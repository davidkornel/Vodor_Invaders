package main.kotlin.com.invaders.view

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.text.Text
import main.kotlin.com.invaders.Game
import main.kotlin.com.invaders.controller.Controller
import main.kotlin.com.invaders.controller.PotatoGridController
import main.kotlin.com.invaders.getResource
import main.kotlin.com.invaders.model.player.Player

class View(
        private val graphicsContext: GraphicsContext,
        private val mainScene: Scene,
        private val gameRoot: Group,
        private val deadRoot: DeadRoot) {

    private val background = Image(getResource("/background.png"))
    private var score: Text = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 10.0
        y = 20.0
    }
    private var beerScore = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 10.0
        y = 40.0
    }
    private var drumstickScore = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 10.0
        y = 60.0
    }
    private var healthPoint = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 10.0
        y = 80.0
    }
    private var lvlOfWeapon = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 10.0
        y = 100.0
    }
    private var cost = Text().apply {
        fill = Color.DARKTURQUOISE
        x = 230.0
        y = 980.0
        text = """
            Upgrade cost
            Beer: 4
            Drumstick: 8
        """.trimIndent()
    }

    init {
        gameRoot.children.add(score)
        gameRoot.children.add(beerScore)
        gameRoot.children.add(drumstickScore)
        gameRoot.children.add(healthPoint)
        gameRoot.children.add(lvlOfWeapon)
        gameRoot.children.add(cost)
    }

    fun oneTickRender(
            player: Player,
            potatoGridController: PotatoGridController,
            controller: Controller) {

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, Game.WIDTH.toDouble(), Game.HEIGHT.toDouble())

        // draw background
        graphicsContext.drawImage(background, 0.0, 0.0)

        // draw player
        graphicsContext.drawImage(player.image, player.playerX.toDouble(), player.playerY.toDouble())

        //draw blasts
        for (b in player.beams) {
            graphicsContext.drawImage(b.image, b.posX, b.posY)
        }

        //draw potato grid
        for (p in potatoGridController.firstPotatoGridWave) {
            graphicsContext.drawImage(p.image, p.posX, p.posY)
        }

        //draw next potato grid wave
        for (p in potatoGridController.nextPotatoGridWave) {
            graphicsContext.drawImage(p.image, p.posX, p.posY)
        }


        //draw potato lasers
        for (l in potatoGridController.laser) {
            graphicsContext.drawImage(l.image, l.posX, l.posY)
        }

        //draw beers
        for (b in controller.getBeers()) {
            graphicsContext.drawImage(b.image, b.posX, b.posY)
        }

        //draw drumsticks
        for (ds in controller.getDrumstick()) {
            graphicsContext.drawImage(ds.image, ds.posX, ds.posY)
        }

        //draw scoreboard
        setScoreboard(controller.score, controller.collectedBeers, controller.collectedDrumsticks, player.healthPoints, player.lvlOfWeapon)
    }

    fun oneTickRenderWhilePlayerIsDead(score: Int) {
        if (deadRoot.shouldChangeRoot) {
            println("change")
            mainScene.root = gameRoot
            deadRoot.shouldChangeRoot = false
        } else {
            deadRoot.setScore(score)
            mainScene.root = deadRoot
        }
    }

    private fun setScoreboard(cScore: Int, cBeers: Int, cDrumsticks: Int, hp: Int, lvl: Int) {
        score.text = "Score: $cScore"
        beerScore.text = "Beer: $cBeers"
        drumstickScore.text = "Drumstick: $cDrumsticks"
        healthPoint.text = "HP: $hp"
        lvlOfWeapon.text = "Weapon level: $lvl"
    }
}
