package main.kotlin.com.invaders.view

import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.kotlin.com.invaders.controller.Controller
import main.kotlin.com.invaders.getResource

class DeadRoot(controller: Controller) : Group() {
    var shouldChangeRoot = false

    val image = Image(getResource("/coffin.png"))
    private val newGameImage = Image(getResource("/startnewgame.png"))
    private val newGameImageView = ImageView(newGameImage)
    private val finalScore = Text().apply {
        fill = Color.WHITESMOKE
        x = 350.0
        y = 900.0
        font = Font.loadFont(getResource("/ARCADECLASSIC.TTF"), 30.0)
    }

    init {
        newGameImageView.x = 312.0
        newGameImageView.y = 800.0
        newGameImageView.isPickOnBounds = true
        newGameImageView.setOnMouseClicked {
            shouldChangeRoot = true
            controller.newGame()
        }
        this.children.add(ImageView(image))
        this.children.add(newGameImageView)
        this.children.add(finalScore)
    }

    fun setScore(score: Int) {
        finalScore.text = "Your  final score is $score"
    }
}

