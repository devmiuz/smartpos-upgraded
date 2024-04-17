package uz.uzkassa.smartpos.core.data.source.resource.user.role.model

import uz.uzkassa.smartpos.core.utils.enums.EnumCompanion
import uz.uzkassa.smartpos.core.utils.enums.valueOrDefault

data class UserRole internal constructor(
    val code: Code,
    val type: Type,
    val priority: Int,
    val nameRu: String
) {

    constructor(
        code: String,
        type: String,
        priority: Int,
        nameRu: String
    ) : this(
        code = Code.valueOrDefault(code),
        type = Type.valueOrDefault(type),
        priority = priority,
        nameRu = nameRu
    )

    companion object {
        val UNKNOWN: UserRole = UserRole(Code.UNKNOWN, Type.UNKNOWN, -1, "")
    }

    enum class Type {
        AUDITOR,
        BRANCH_ADMIN,
        CASHIER,
        INTERN,
        OWNER,
        WAREHOUSE_MANAGER,
        UNKNOWN;
        
        companion object : EnumCompanion<Type> {
            override val DEFAULT: Type = UNKNOWN
        }
    }

    enum class Code {
        ROLE_AUDITOR,
        ROLE_BRANCH_ADMIN,
        ROLE_OWNER,
        ROLE_CASHIER,
        ROLE_INTERN,
        ROLE_WAREHOUSE_MANAGER,
        UNKNOWN;

        companion object : EnumCompanion<Code> {
            override val DEFAULT: Code = UNKNOWN
        }
    }
}