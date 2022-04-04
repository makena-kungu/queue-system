class LinearCongruential(seed: Int) {
    private val m = 2000
    private val a = 676// this numbers can be generated randomly
    private val c = 76// same as this

    private var x: Int

    init {
        x = seed
    }

    constructor() : this(9)

    @Suppress("unused")
    fun setSeed(seed: Int) {
        x = seed
    }

    /**
     * Initiates the generation of the next random numbers and returns the random number in the range [0, m-1]/m
     */
    fun next(): Double {
        x = (a * x + c).mod(m)
        return x / m.toDouble()
    }
}