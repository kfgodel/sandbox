package info.kfgodel.contable

import java.time.LocalDateTime

/**
 * Date: 26/6/22 - 23:31
 */
fun on(day: Int, month: Int, year: Int): LocalDateTime {
    return LocalDateTime.of(year, month, day, 0, 0)
}