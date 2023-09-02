package info.kfgodel.contable.reader

import info.kfgodel.contable.concepts.Operation
import java.io.File

/**
 * This type represents a file reader that can get operations out of the file contents
 * Date: 2/9/23 - 18:02
 */
interface OperationsReader {
  fun addReportFile(reportFile: File): OperationsReader
  fun operations(): List<Operation>
}