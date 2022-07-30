package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import java.math.BigDecimal

/**
 * Date: 29/7/22 - 00:34
 */
class CombinedValueAsset(private val first: ValuedAsset, private val second: ValuedAsset) : ValuedAsset {
    override fun asset(): Magnitude {
        return first.asset().sum(second.asset())
    }

    override fun value(): Magnitude {
        val firstAmount = first.asset().amount
        val secondAmount = second.asset().amount
        if(firstAmount.signum().equals(secondAmount.signum())){
            // Asset increases, so does value, even on negative direction
            return first.value().sum(second.value())
        }
        // There's a reduction of asset. We need to see which of the assets still stands and use that asset's value
        val remainingAsset = calculateRemaining(firstAmount, secondAmount)
        return remainingAsset.value()
    }

    private fun calculateRemaining(
        firstAmount: BigDecimal,
        secondAmount: BigDecimal
    ): ValuedAsset {
        if (firstAmount.abs() >= secondAmount.abs()) {
            // After reduction, we still have some of the first asset
            val remaining = firstAmount.add(secondAmount)
            return first.proportionalTo(remaining)
        }

        val remaining = secondAmount.add(firstAmount)
        return second.proportionalTo(remaining)
    }
}