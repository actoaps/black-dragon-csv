package dk.acto.web

import java.util.*
import kotlin.math.abs

class Value(random: Random) {
    val id: Long = abs(random.nextLong())
    val inventory: Int = random.nextInt(10000)
    val weight: Double = random.nextDouble()
    val cost: Int = (random.nextInt(20) + 1) * 100
}