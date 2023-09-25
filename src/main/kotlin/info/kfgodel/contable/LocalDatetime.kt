package info.kfgodel.contable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Date: 26/6/22 - 23:31
 */
fun on(dayOfMonth: Int, month: Int, year: Int): LocalDateTime {
    return LocalDateTime.of(year, month, dayOfMonth, 0, 0)
}

/**
 * Returns an ISO formatted string containing only the date part
 */
fun LocalDateTime.toISODate(): String {
  return this.format(DateTimeFormatter.ISO_DATE)
}
