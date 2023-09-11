package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.concepts.OperationType
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
      .filter { balance -> balance.hasAsset() || !balance.value().isZero() }
  }

  fun include(included: Operation): List<ValueChange> {
//    validate(included)
    var currentOp = included
    if(currentOp.value().unit != valueUnit && currentOp.value().amount.signum() < 0){
      // It's an expense in other currency, let's invert the operation to operate over the currency itself
      currentOp = currentOp.counterpart()
    }
    val includedAssetUnit = currentOp.asset().unit
    val assetBalance = getBalanceFor(includedAssetUnit)
    val assetChanges = assetBalance.updateWith(currentOp)
    considerProfitAndLossesDueTo(assetChanges)

    val valueOperations = calculateValueOperations(currentOp, assetChanges)
      .filter { operation -> operation.hasAsset() }
    valueOperations.forEach { valueOperation ->
      val valueBalance = getBalanceFor(valueOperation.asset().unit)
      val valueChanges = valueBalance.updateWith(valueOperation)
    }
    return assetChanges
  }

  private fun calculateValueOperations(included: Operation, assetChanges: List<ValueChange>): List<Operation> {
    val includedValueUnit = included.value().unit
    if(includedValueUnit == valueUnit || assetChanges.isEmpty()){
      // Operation uses same currency as this portfolio, use the value as is
      return mutableListOf(included.counterpart())
    }
    // There's an implicit currency conversion operation per change, we need to create a relationship
    // between new currency and old value
    return assetChanges.map { change ->
      Operation(OperationType.BUY, change.replacement().value().at(change.replaced().value()),change.replacement().moment, change.replacement().mainAccount, change.replacement().externalAccount)
    }
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

  fun totalValue(): Magnitude {
    var total = 0.of(valueUnit)
    balances().forEach { balance ->
      val valueToSum = if(balance.asset().unit == valueUnit) balance.asset() else balance.value().negative()
      total = total.sum(valueToSum)
    }
    return total
  }

  override fun toString(): String {
    return "PortfolioValuation[${totalValue()}]: ${balances()}"
  }
}
