package info.kfgodel.contable

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
        val reduced = assetBalance.updateWith(included)
        considerProfitAndLossesDueTo(reduced, included)
    }

    private fun considerProfitAndLossesDueTo(removed: List<ValuedAsset>, replacement: ValuedAsset) {
        removed.forEach { previous ->
            val change = ValueChange(previous, replacement)
            if (change.isZero()) {
                // Nor profit or loss
            } else {
                profitAndLosses.add(change)
            }
        }
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

}