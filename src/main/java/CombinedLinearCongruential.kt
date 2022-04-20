class CombinedLinearCongruential(seed1: Long, seed2: Long) {
    private val gen1 = Generator(a = 134, x = seed1, m = 980435489)
    private val gen2 = Generator(a = 344, x = seed2, m = 4392780524)

    //the default constructor has two seeds
    constructor() : this(23657, 4544)

    fun next(): Double {
        gen1.x = (gen1.a * gen1.x) % gen1.m
        gen2.x = (gen2.a * gen2.x) % gen2.m

        return when (val xi = (gen1 - gen2).mod(gen1.m - 1)) {
            0L -> (gen1.m - 1) / gen1.m.toDouble()
            else -> xi / gen1.m.toDouble()
        }
    }

    data class Generator(
        val a: Long, var x: Long, val m: Long
    ) {
        operator fun minus(other: Generator) = x - other.x
    }
}