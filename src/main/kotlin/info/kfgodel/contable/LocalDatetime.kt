package info.kfgodel.contable

import java.time.LocalDateTime

/**
 * Date: 26/6/22 - 23:31
 */
fun on(dayOfMonth: Int, month: Int, year: Int): LocalDateTime {
    return LocalDateTime.of(year, month, dayOfMonth, 0, 0)
}