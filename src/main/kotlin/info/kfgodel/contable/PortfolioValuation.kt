package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation
import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.contable.valued.ValueChange
import info.kfgodel.contable.valued.ValuedAsset
import java.util.TreeMap

/**
 * This class represents a portfolio of assets valued at a given time
 * Date: 28/7/22 - 22:25
 */
class PortfolioValuation(val valueUnit: String) {

    private val valuesPerAssetUnit = TreeMap<String, AssetBalance>()
    private val profitAndLosses = mutableListOf<ValueChange>()

    fun balances(): List<AssetBalance> {
        return valuesPerAssetUnit.values.toList()
    }

    fun include(included: ValuedAsset) {
        validate(included)
        val includedAssetUnit = included.asset().unit
        val assetBalance = valuesPerAssetUnit.computeIfAbsent(includedAssetUnit) { assetUnit ->
            AssetBalance(
                assetUnit,
                valueUnit
            )
        }
        val changes = assetBalance.updateWith(included)
        considerProfitAndLossesDueTo(changes)
    }

    private fun considerProfitAndLossesDueTo(changes: List<ValueChange>) {
        changes
            .filter { change -> !change.isZero() } // Exclude changes that are not profit or losses
            .forEach(profitAndLosses::add)
    }

    private fun validate(valued: ValuedAsset) {
        val newUnit = valued.value().unit
        if(newUnit != valueUnit){
            throw UnsupportedOperationException("Include using a different value[$newUnit] than expected[$valueUnit]")
        }
    }

    fun profitAndLosses(): List<ValuedAsset> {
        return profitAndLosses
    }

    fun removeProfitAndLosses() {
        this.profitAndLosses.clear()
    }

    fun includeAll(operations: Iterable<Operation>) {
        operations.forEach(this::include)
    }

}