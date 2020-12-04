package main.kotlin.com.invaders

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import main.kotlin.com.invaders.controller.Controller
import main.kotlin.com.invaders.controller.PotatoGridController
import main.kotlin.com.invaders.controller.button.PauseButton
import main.kotlin.com.invaders.controller.button.UpgradeButton
import main.kotlin.com.invaders.model.player.Player
import main.kotlin.com.invaders.view.DeadRoot
import main.kotlin.com.invaders.view.View

class Game : Application() {

    companion object {
        internal const val WIDTH = 1024
        internal const val HEIGHT = 1024
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext


    //private lateinit var space: Image
    //player
    private var player = Player()
    //width 8 height 4
    private var potatoGrid = PotatoGridController(10)
    //buttons
    private val pauseButton = PauseButton()
    private val upgradeButton = UpgradeButton()
    //game controller
    private val controller = Controller(player, potatoGrid, upgradeButton)
    //view
    private lateinit var view: View
    private var lastFrameTime: Long = System.nanoTime()
    // use a set so duplicates are not possible
    private val currentlyActiveKeys = mutableSetOf<KeyCode>()
    //roots
    private lateinit var gameRoot: Group
    private var deadRoot = DeadRoot(controller)

    override fun start(mainStage: Stage) {
        mainStage.title = "Vodor Invaders"
        gameRoot = Group()
        gameRoot.isMouseTransparent = false
        mainScene = Scene(gameRoot as Parent?)

        mainStage.scene = mainScene

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())

        gameRoot.children.add(canvas)
        gameRoot.children.add(pauseButton)
        gameRoot.children.add(upgradeButton)

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D
        view = View(graphicsContext, mainScene, gameRoot, deadRoot)

        // Main loop
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRenderGame(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            currentlyActiveKeys.add(event.code)
        }
        mainScene.onKeyReleased = EventHandler { event ->
            currentlyActiveKeys.remove(event.code)
        }
    }

    private fun tickAndRenderGame(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime
        if (!pauseButton.gamePaused && controller.isAlive) {
            //controller moves object, checks for hits, check for any collision
            controller.oneTick(currentlyActiveKeys, elapsedNanos)
        }

        if (controller.isAlive && !deadRoot.shouldChangeRoot) {
            view.oneTickRender(player, potatoGrid, controller)
        } else {
            view.oneTickRenderWhilePlayerIsDead(controller.score)
        }

        // display crude fps counter
        //TODO: poor af
        //val elapsedMs = elapsedNanos / 1_000_000
        //graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)


    }

    /* private fun tickAndRenderShopMenu(currentNanoTime: Long) {

     }*/


}