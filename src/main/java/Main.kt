import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.RegionUtil
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt

fun main() {
    /*val numberOfCustomers = 10
    val customerData = generateCustomerData(numberOfCustomers)
    val generator = QueueGenerator(*customerData.toTypedArray())
    generator.customerData().apply {
        print()
        createWorkBook()
    }*/

    val l = listOf(
        "firstName",
        "middleName",
        "lastName",
        "dob",
        "memNo",
        "mobileNo",
        "genderGroup",
        "isStaff",
        "clientType",
        "clientClassification",
        "submitDate",
        "nationalId",
        "email",
        "activationDate"
    )

    l.forEach { s ->
        s.map {
            if (it.isUpperCase()) {
                "_$it"
            } else it
        }.joinToString("").also {
            val title = "KEY_${it.uppercase()}"
            println("const val $title = \"$s\"")
        }
    }
}

fun generateCustomerData(n: Int): List<Customer> {
    val customers = mutableListOf<Customer>()
    val linearRandom = LinearCongruential()
    val combinedRandom = CombinedLinearCongruential()
    //The random generators are working
    for (i in 0 until n) {
        val lRandom = linearRandom.next()
        var c = .0
        val interArrivalTime = if (i == 0) 0 else (lRandom * 10).roundToInt()
        val serviceTime = run {
            var time: Int
            do {
                val cRandom = combinedRandom.next()
                c = cRandom
                time = (cRandom * 8).roundToInt()
            } while (time == 0)
            time
        }
        println("randoms = ($lRandom, $c)")

        customers += Customer(interArrivalTime, serviceTime)
    }
    return customers
}

fun List<Queue>.print() {
    forEach {
        println(it)
    }
}

fun List<Queue>.createWorkBook() {
    try {
        val workbook = XSSFWorkbook()
        workbook.createSheet(this)

        val outputStream = FileOutputStream("Queue System.xlsx")
        workbook.write(outputStream)

        outputStream.close()
        workbook.close()
        println("Workbook Created Successfully!")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun XSSFWorkbook.createSheet(data: List<Queue>) {
    val sheet = createSheet("Safaricom's Queue")

    val headerRow = sheet.createRow(0)
    headerRow.rowStyle = hStyle
    columnHeadings.forEachIndexed { index, heading ->
        val cell = headerRow.createCell(index, CellType.STRING)
        cell.cellStyle = hStyle
        cell.setCellValue(heading)
    }

    //filling in the data
    var k = 0
    var waitingtime = 0
    var servicetime = 0
    var timespent = 0
    var prob = 0
    data.forEachIndexed { i, queue ->
        val row = sheet.createRow(i + 1)
        row.apply {
            queue.apply {
                var j = 0
                createCell(j++, customer.toString())
                createCell(j++, interArrivalTime.toString())
                createCell(j++, arrivalTime.toString())
                createCell(j++, serviceTime.toString())
                createCell(j++, timeServiceBegins.toString())
                createCell(j++, waitingTimeInQueue.toString())
                createCell(j++, timeServiceEnds.toString())
                createCell(j++, timeCustomerSpendsInTheSystem.toString())
                createCell(j, idleTime.toString())

                waitingtime += waitingTimeInQueue
                servicetime += serviceTime
                timespent += timeCustomerSpendsInTheSystem
                if (idleTime > 0) prob++
            }
        }
        k = i
    }

    val size = data.size.toDouble()
    val map = mapOf(
        "Average Waiting Time" to waitingtime / size,
        "Average Service Time" to servicetime / size,
        "Average Time Spent" to timespent / size,
        "Probability That A customer will wait in queue" to prob / size
    )

    map.forEach { (key, value) ->
        sheet.createRow(++k).apply {
            createCell(0, key)
            createCell(1, value.toString())
        }
    }

    val cRange = CellRangeAddress(0, data.size, 0, columnHeadings.size - 1)
    val bStyle = BorderStyle.MEDIUM
    RegionUtil.setBorderLeft(bStyle, cRange, sheet)
    RegionUtil.setBorderTop(bStyle, cRange, sheet)
    RegionUtil.setBorderRight(bStyle, cRange, sheet)
    RegionUtil.setBorderBottom(bStyle, cRange, sheet)

}

fun XSSFRow.createCell(index: Int, data: String) {
    val cell = createCell(index)
    cell.cellStyle.apply {
        val bStyle = BorderStyle.THIN
        borderTop = bStyle
        borderLeft = bStyle
        alignment = HorizontalAlignment.CENTER
    }.also {
        cell.cellStyle = it
    }
    cell.setCellValue(data)
}

private val XSSFWorkbook.hFont: XSSFFont
    get() {
        val font = createFont()
        font.bold = true
        return font
    }

private val XSSFWorkbook.hStyle: XSSFCellStyle
    get() {
        val style = createCellStyle()
        style.wrapText = true
        style.setFont(hFont)
        return style
    }

private val columnHeadings
    get() = arrayOf(
        "Customer",
        "Inter-Arrival Time(min)",
        "Arrival Time",
        "Service Time (Min)",
        "Time Service Begins",
        "Waiting Time",
        "Service End Time",
        "Time Spent",
        "Idle Time"
    )

data class Customer(
    val interArrivalTime: Int, val serviceTime: Int
)

data class Queue(
    val customer: Int,
    val interArrivalTime: Int,
    val arrivalTime: Int,
    val serviceTime: Int,
    val timeServiceBegins: Int,
    val waitingTimeInQueue: Int,
    val timeServiceEnds: Int,
    val timeCustomerSpendsInTheSystem: Int,
    val idleTime: Int
)