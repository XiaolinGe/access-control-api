package com.cfgglobal.test.dao

import com.cfgglobal.test.dao.base.BaseDao
import com.cfgglobal.test.domain.RolePermission
import org.springframework.stereotype.Repository

@Repository
interface RolePermissionDao : BaseDao<RolePermission, Long>