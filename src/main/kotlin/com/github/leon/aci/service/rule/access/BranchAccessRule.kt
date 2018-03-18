package com.github.leon.aci.service.rule.access


import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Filter
import org.springframework.stereotype.Component

@Deprecated("")
@Component
class BranchAccessRule : AbstractAccessRule() {

    override val ruleName: String
        get() = "branch"

    override fun exec(permission: Permission): Filter {
        val branch = securityFilter!!.currentUser().branch
        val orgFilter = Filter()
        val orgId = branch!!.id
        orgFilter.addCondition("creator.branch.id", orgId, Filter.OPERATOR_EQ)
        return orgFilter
    }
}