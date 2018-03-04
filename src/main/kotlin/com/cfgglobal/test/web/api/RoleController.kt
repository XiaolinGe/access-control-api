package com.cfgglobal.test.web.api

import com.cfgglobal.test.config.json.JsonConfig
import com.cfgglobal.test.domain.Permission
import com.cfgglobal.test.domain.Role
import com.cfgglobal.test.domain.RolePermission
import com.cfgglobal.test.domain.Rule
import com.cfgglobal.test.service.PermissionService
import com.cfgglobal.test.service.RoleService
import com.cfgglobal.test.util.Q
import com.cfgglobal.test.web.base.BaseController
import com.github.leon.bean.JpaBeanUtil
import com.querydsl.core.types.Path
import io.vavr.collection.List
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(value = ["/v1/role"])

class RoleController : BaseController() {

    @Autowired
    private val roleService: RoleService? = null

    @Autowired
    private val permissionService: PermissionService? = null


    private val IGNORE_AUDITOR: kotlin.collections.List<Path<*>> = listOf(Q.baseEntity.creator,
            Q.baseEntity.createdAt,
            Q.baseEntity.updatedAt,
            Q.baseEntity.modifier
    )

    private val CLEAN_ROLE = JsonConfig.start().exclude(Role::class.java, *IGNORE_AUDITOR.toTypedArray(), Q.role.users)
            .exclude(RolePermission::class.java, *IGNORE_AUDITOR.toTypedArray())
            .exclude(Permission::class.java, *IGNORE_AUDITOR.toTypedArray())
            .exclude(Rule::class.java, *IGNORE_AUDITOR.toTypedArray())

    @GetMapping
    fun list(pageable: Pageable): ResponseEntity<Page<Role>> {
        return ResponseEntity.ok(roleService!!.findAll(pageable))
    }


    @GetMapping("{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Role> {
        val role = roleService!!.findOne(id).get()
        val selectedRolePermissions = List.ofAll(role.rolePermissions)
        val list = permissionService!!.findAll()
                .map { permission ->
                    RolePermission(
                            permission = permission,
                            rules = selectedRolePermissions
                                    .filter { (permission1) -> permission1!!.id == permission!!.id }
                                    .toOption()
                                    .map { it.rules }
                                    .getOrElse { mutableListOf() })

                }
        role.copy(rolePermissions = list.toMutableList())
        CLEAN_ROLE.end()
        return ResponseEntity.ok(role)

    }

    @PostMapping
    @Transactional
    fun role(@RequestBody role: Role): ResponseEntity<Role> {
        roleService!!.save(role)
        return ResponseEntity.ok(role)
    }


    @PutMapping("{id}")
            // @Transactional
    fun role(@PathVariable id: Long, @RequestBody role: Role, request: HttpServletRequest): ResponseEntity<Role> {

        val cleanedRole = roleService!!.removeEmptyRules(role)
        val oldRole = roleService.findOne(id).get()
        JpaBeanUtil.copyNonNullProperties(cleanedRole, oldRole)
        cleanedRole.rolePermissions.forEach({ this.syncFromDb(it) })
        oldRole.rolePermissions.clear()
        oldRole.rolePermissions.addAll(cleanedRole.rolePermissions)
        roleService.saveBySecurity(oldRole, request.method, request.requestURI)
        return ResponseEntity.ok(oldRole)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        roleService!!.deleteBySecurity(id, request.method, request.requestURI)
        return ResponseEntity.ok().build<Any>()
    }


}