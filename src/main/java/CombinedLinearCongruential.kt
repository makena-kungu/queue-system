class CombinedLinearCongruential(vararg seeds: Long) {
    private val generators = mutableListOf<Generator>()

    init {
        seeds.forEach { seed ->
            val m = (seed..Long.MAX_VALUE).random()
            val range = 1 until m
            generators += Generator(
                a = range.random(), x = seed, m = range.random()
            )
        }
    }

    @Suppress("unused")
    //the default constructor has two seeds
    constructor() : this(123, 346)

    fun next(): Double {
        val first = generators.first()
        var sum = 0L
        generators.forEach {
            it.apply {
                x = (a * x).mod(m)
                sum -= x
            }
        }
        val m = first.m.toDouble()
        val mMinusOne = m.toLong()
        return when (val xi = sum.mod(mMinusOne)) {
            0L -> mMinusOne/m
            else -> xi / m
        }
    }

    data class Generator(
        val a: Long, var x: Long, val m: Long
    )
}