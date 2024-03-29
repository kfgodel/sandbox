package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.of
import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.contable.valued.ValueChange
import info.kfgodel.contable.valued.ValuedAsset
import java.util.TreeMap

/**
 * This class represents a portfolio of assets valued at a given time
 * Date: 28/7/22 - 22:25
 */
class PortfolioValuation(val valueUnit: String) {

  private val balancePerAsset = TreeMap<String, AssetBalance>()
  private val profitAndLosses = mutableListOf<ValueChange>()

  fun balances(): List<AssetBalance> {
    return balancePerAsset.values.toList()
  }

  fun include(included: Operation): List<ValueChange> {
    validate(included)
    val includedAssetUnit = included.asset().unit
    val assetBalance = getBalanceFor(includedAssetUnit)
    val changes = assetBalance.updateWith(included)
    considerProfitAndLossesDueTo(changes)
    return changes
  }

  private fun getBalanceFor(includedAssetUnit: String): AssetBalance {
    return balancePerAsset.computeIfAbsent(includedAssetUnit) { assetUnit ->
      AssetBalance(assetUnit, valueUnit)
    }
  }

  private fun considerProfitAndLossesDueTo(changes: List<ValueChange>) {
    changes
      .filter { change -> !change.isZero() } // Exclude changes that are not profit or losses
      .forEach(profitAndLosses::add)
  }

  private fun validate(valued: ValuedAsset) {
    val newUnit = valued.value().unit
    if (newUnit != valueUnit) {
      throw UnsupportedOperationException("Include using a different value[$newUnit] than expected[$valueUnit]: $valued")
    }
  }

  fun removeProfitAndLosses() {
    this.profitAndLosses.clear()
  }

  fun includeAll(operations: Iterable<Operation>): List<AccountantRecord> {
    return operations.map { operation ->
      val changes = this.include(operation)
      AccountantRecord(operation, changes)
    }
  }

  fun totalProfitOrLoss(): Magnitude {
    return profitAndLosses.fold(0.of(valueUnit)) { total, change -> total.sum(change.value()) }
  }

  override fun toString(): String {
    return "PortfolioValuation[${valueUnit}]: ${balances()}"
  }
}
