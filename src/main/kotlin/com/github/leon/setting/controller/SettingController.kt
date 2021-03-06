package com.github.leon.setting.controller

import com.github.leon.aci.web.base.BaseController
import com.github.leon.setting.domain.Setting
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/setting")
class SettingController : BaseController<Setting, Long>() {

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<Setting>> {
        return super.page(pageable, request)
    }
    @GetMapping("{id}")
    override fun findOne(id: Long, request: HttpServletRequest): ResponseEntity<Setting> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(input: Setting, request: HttpServletRequest): ResponseEntity<*> {
        return super.saveOne(input, request)
    }

    @PutMapping
    override fun updateOne(id: Long, input: Setting, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }
}