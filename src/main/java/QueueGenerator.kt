class QueueGenerator(private vararg val customers: Customer) {
    //the arrival, begin service and end service times are done cumulatively
    //idle time is calculated by arrival time - prev service end time
    private val data = mutableListOf<Queue>()

    fun customerData(): List<Queue> {
        data.clear()
        customers.forEachIndexed { i, customer ->
            val isFirst = i == 0
            val previous: Queue? = if (isFirst) null else data.last()

            val interArrivalTime = customer.interArrivalTime
            val arrivalTime = previous?.run { arrivalTime + interArrivalTime } ?: 0
            val timeServiceBegins = previous?.run {
                when {
                    timeServiceEnds <= arrivalTime -> arrivalTime
                    else -> timeServiceEnds
                }
            } ?: 0
            val serviceTime = customer.serviceTime
            val timeServiceEnds = timeServiceBegins + serviceTime
            val timeSpent = timeServiceEnds - arrivalTime

            val queue = Queue(
                i + 1,
                interArrivalTime,
                arrivalTime,
                serviceTime,
                timeServiceBegins,
                timeServiceBegins - arrivalTime,
                timeServiceEnds,
                timeSpent,
                previous?.let {
                    val d = arrivalTime - it.timeServiceEnds
                    if (d > 0) d else 0
                } ?: 0
            )
            data += queue
        }
        return data
    }
}