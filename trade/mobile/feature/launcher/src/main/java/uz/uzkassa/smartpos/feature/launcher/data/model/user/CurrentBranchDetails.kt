package uz.uzkassa.smartpos.feature.launcher.data.model.user

import uz.uzkassa.smartpos.core.data.source.resource.branch.model.branch.Branch
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company

internal class CurrentBranchDetails(val company: Company, val branch: Branch)