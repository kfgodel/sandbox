package info.kfgodel.contable

import info.kfgodel.contable.valued.CombinedValueAsset
import info.kfgodel.contable.valued.ValuedAsset
import java.util.TreeMap

/**
 * This class represents a portfolio of assets valued at a given time
 * Date: 28/7/22 - 22:25
 */
class PortfolioValuation {

    private val valuesPerAssetUnit = TreeMap<String, ValuedAsset>()

    fun valuedAssets(): List<ValuedAsset> {
        return valuesPerAssetUnit.values.toList()
    }

    fun include(valued: ValuedAsset) {
        val includedAssetUnit = valued.asset().unit
        val previousValue = valuesPerAssetUnit.putIfAbsent(includedAssetUnit, valued)
        if(previousValue != null){
            val combinedValue = combineWith(previousValue, valued)
            valuesPerAssetUnit[includedAssetUnit] = combinedValue
        }
    }

    private fun combineWith(previous: ValuedAsset, valued: ValuedAsset): ValuedAsset {
        return CombinedValueAsset(previous, valued)
    }
}