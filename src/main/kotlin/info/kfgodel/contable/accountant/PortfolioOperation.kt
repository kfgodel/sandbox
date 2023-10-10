package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Exchange
import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.valued.ValuedAsset
import java.math.BigDecimal

/**
 * This class represents an operation that is part of a portfolio valuation
 * Date: 8/10/23 - 20:05
 */
class PortfolioOperation(private val asset: Magnitude, private val valuation: Magnitude, val originalOperation: Operation) : ValuedAsset<PortfolioOperation> {
  override fun asset(): Magnitude {
    return asset
  }

  override fun value(): Magnitude {
    return valuation
  }

  private fun exchange() : Exchange{
    return asset().at(value())
  }

  override fun splitBy(splitAmount: BigDecimal): Pair<PortfolioOperation, PortfolioOperation> {
    val (split, remaining) = exchange().splitBy(splitAmount)
    // Should we keep a ref to this portfolio op instead of the original op?
    return Pair(
      PortfolioOperation(split.asset(), split.value(), originalOperation),
      PortfolioOperation(remaining.asset, remaining.value(), originalOperation)
    )
  }

  override fun toString(): String {
    val valueExpression = if (asset() == value()) "${asset()}" else "${asset()} @ ${value()}"
    return "$valueExpression from [$originalOperation]"
  }
}