package uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation

import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.R
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.ResourceString
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource

enum class CashOperation : LocalizableResource {
    PAID,
    RETURNED,
    INCOME,
    WITHDRAW,
    FLOW,
    RETURN_FLOW,
    INCASSATION,
//    CREDIT,
//    ADVANCE,
    UNKNOWN;

    override val resourceString: ResourceString by lazy {
        val resourceId: Int = when (this) {
            INCOME ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_added_cash
            FLOW ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_consumption
            INCASSATION ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_encashment
            PAID ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_paid
            RETURNED ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_returned
            WITHDRAW ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_returned_added_cash
            RETURN_FLOW ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_returned_consumption
//            CREDIT ->
//                R.string.fragment_feature_user_cashier_cash_operations_operation_credit
//            ADVANCE ->
//                R.string.fragment_feature_user_cashier_cash_operations_operation_advance
            else ->
                R.string.fragment_feature_user_cashier_cash_operations_operation_unknown
        }

        return@lazy StringResource(resourceId)
    }
}