import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.truncate

class MidSquare(seed: Int = 7182) {

    private var z: Long = seed.toLong()

    fun next(): Double {
        val z2 = z.toDouble().pow(2.0).toLong()
        z = z2.let {
            val removeLast = truncate(it / 100.0)
            val value = removeLast / 10000
            val summand = truncate(value).toLong()
            (abs(value - summand) * 10000).roundToLong()
        }
        //println(z)
        return z / 10000.0
    }
}

val midSquare = MidSquare(2154)
fun main() {
    /*repeat(20) {
        println("$it ${midSquare.next()}")
    }*/

    val monteCarloSimulation = MonteCarloSimulation()
    println("pi = ${monteCarloSimulation.pi()}")
}