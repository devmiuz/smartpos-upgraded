package uz.uzkassa.smartpos.feature.launcher.data.model.user

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language
import uz.uzkassa.smartpos.core.data.source.resource.user.model.User

internal data class UsersAuth(
    val company: Company,
    val branch: Branch,
    val ownerLanguage: Language,
    val cashiers: List<User>,
    val users: List<User>
)