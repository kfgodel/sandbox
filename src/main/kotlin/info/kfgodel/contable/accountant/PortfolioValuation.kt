package info.kfgodel.contable.accountant

import info.kfgodel.contable.calculator.ChangeCalculator
import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.of
import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.contable.valued.ValueChange
import java.util.TreeMap

/**
 * This class represents a portfolio of assets valued at a given time
 * Date: 28/7/22 - 22:25
 */
class PortfolioValuation(private val valueUnit: String) {

  private val balancePerAsset = TreeMap<String, AssetBalance>()
  private val profitAndLosses = mutableListOf<ValueChange<PortfolioOperation>>()

  fun balances(): List<AssetBalance> {
    return balancePerAsset.values.toList()
      .filter { balance -> !balance.asset().isZero() || !balance.value().isZero() }
  }

  fun include(included: Operation): AccountantRecord {
    val portfolioOperations = valuateExchange(included)
    val record = AccountantRecord(included, totalValue())
    for (portfolioOperation in portfolioOperations) {
      val assetBalance = getBalanceFor(portfolioOperation.asset().unit)
      val updatedValues = assetBalance.updateWith(portfolioOperation)
      record.addAll(updatedValues);
    }
    considerProfitAndLossesDueTo(record.changes)
    record.finalValuation(totalValue())
    return record
  }

  private fun valuateExchange(included: Operation): List<PortfolioOperation> {
    val valuated = mutableListOf<PortfolioOperation>()
    if (included.hasAsset()) {
      valuated.add(createPortfolioOp(included))
    }
    val valueOperation = included.counterpart()
    if (valueOperation.hasAsset() && valueOperation.asset().unit != included.asset().unit) {
      // Add a second operation if 2 different units need to update their balance
      valuated.add(createPortfolioOp(valueOperation))
    }
    return valuated
  }

  private fun createPortfolioOp(operation: Operation): PortfolioOperation {
    val valuation: Magnitude = calculateValueFor(operation)
    return PortfolioOperation(operation.asset(), valuation, operation)
  }

  private fun calculateValueFor(operation: Operation): Magnitude {
    val operationValue = operation.value()
    // If the value it's already expressed in the currency we need, we use it
    if (operationValue.unit == valueUnit) {
      // We only return positive valuation, the asset signum determines its effect
      return operationValue.asPositive()
    }
    val operationAsset = operation.asset()
    // If the asset is the currency we
    if (operationAsset.unit == valueUnit) {
      // The asset is already expressed on the currency we need
      return operation.asset().asPositive()
    }
    // Using a foreign currency. The only way to valuate is using the value of the exchanged asset present in existing balances
    val estimatedAssetValuation = estimateValueUsingBalances(operationAsset, operation, operationValue)
    if(estimatedAssetValuation != null){
      return estimatedAssetValuation
    }
    val estimatedValueValuation = estimateValueUsingBalances(operationValue, operation, operationAsset)
    if(estimatedValueValuation != null){
      return estimatedValueValuation
    }
    // We can fix this is we have a foreign currency conversion rate for the date as reference
    throw IllegalStateException("We don't know how to valuate unknown asset in $operation")
  }

  private fun estimateValueUsingBalances(
    operationAsset: Magnitude,
    operation: Operation,
    operationValue: Magnitude
  ): Magnitude? {
    val balance = getBalanceFor(operationAsset.unit)
    val result = ChangeCalculator(balance.valuables())
      .calculateFor(PortfolioOperation(operationAsset, operationValue, operation))
    val valueChanges = result.changes()
    if (valueChanges.isEmpty()) {
      // We don't have assets previously valuated to use as reference
      return null
    }
    return valueChanges
      .map { it.first.value() }
      .reduce(Magnitude::sum)
  }

  private fun getBalanceFor(includedAssetUnit: String): AssetBalance {
    return balancePerAsset.computeIfAbsent(includedAssetUnit) { assetUnit ->
      AssetBalance(assetUnit, valueUnit)
    }
  }

  private fun considerProfitAndLossesDueTo(changes: List<ValueChange<PortfolioOperation>>) {
    changes
      .filter { change -> !change.isZero() } // Exclude changes that are not profit or losses
      .forEach(profitAndLosses::add)
  }

  fun removeProfitAndLosses() {
    this.profitAndLosses.clear()
  }

  fun includeAll(operations: Iterable<Operation>): List<AccountantRecord> {
    return operations.map(this::include)
  }

  fun totalProfitOrLoss(): Magnitude {
    return profitAndLosses.fold(0.of(valueUnit)) { total, change -> total.sum(change.value()) }
  }

  fun totalValue(): Magnitude {
    var total = 0.of(valueUnit)
    balances().forEach { balance ->
      val valueToSum = if (balance.asset().unit == valueUnit) balance.asset() else balance.value()
      total = total.sum(valueToSum)
    }
    return total
  }

  override fun toString(): String {
    return "PortfolioValuation[${totalValue()}]: ${balances()}"
  }
}
