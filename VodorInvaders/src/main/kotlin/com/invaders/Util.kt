package main.kotlin.com.invaders

import kotlin.random.Random

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}

fun getChance(percentage: Int): Boolean{
    val number = Random.nextInt(0,100)
    return number <= percentage
}