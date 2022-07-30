package info.kfgodel.contable

import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.contable.valued.ValuedAsset
import java.util.TreeMap

/**
 * This class represents a portfolio of assets valued at a given time
 * Date: 28/7/22 - 22:25
 */
class PortfolioValuation(val valueUnit: String) {

    private val valuesPerAssetUnit = TreeMap<String, AssetBalance>()

    fun balances(): List<AssetBalance> {
        return valuesPerAssetUnit.values.toList()
    }

    fun include(valued: ValuedAsset) {
        val includedAssetUnit = valued.asset().unit
        val assetBalance = valuesPerAssetUnit.computeIfAbsent(includedAssetUnit) { assetUnit ->
            AssetBalance(
                assetUnit,
                valueUnit
            )
        }
        assetBalance.updateWith(valued)
    }

}