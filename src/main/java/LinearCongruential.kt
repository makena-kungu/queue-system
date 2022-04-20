class LinearCongruential(seed: Int) {
    private val m = 4256
    private val a = 349
    private val c = 435

    private var x: Int

    init {
        x = seed
    }

    constructor() : this(1786)

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