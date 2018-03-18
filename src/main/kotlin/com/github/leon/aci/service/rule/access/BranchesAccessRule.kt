package com.github.leon.aci.service.rule.access


import com.github.leon.aci.dao.BranchDao
import com.github.leon.aci.domain.Permission
import com.github.leon.aci.vo.Filter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Deprecated("")
@Component
class BranchesAccessRule : AbstractAccessRule() {
    @Autowired
    private val branchDao: BranchDao? = null

    override val ruleName: String
        get() = "branches"

    override fun exec(permission: Permission): Filter {
        val branch = securityFilter!!.currentUser().branch
        val orgFilter = Filter()
        val orgId = branch!!.id!!
        var ids = branchDao!!.findSubOrgIds(branch.id)
        ids += orgId
        orgFilter.addCondition("creator.branch.id", ids, Filter.OPERATOR_IN)
        return orgFilter
    }
}