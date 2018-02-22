package com.cfgglobal.test.web.api

import com.cfgglobal.test.service.excel.ExcelParsingRule
import com.cfgglobal.test.web.base.BaseController
import com.github.leon.files.PoiImporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.time.Instant


@RestController
@RequestMapping("/v1/excel")

class ExcelController : BaseController() {

    @Autowired
    var excelParsingRules: MutableList<ExcelParsingRule<*>> = mutableListOf()

    @PostMapping
    @Throws(IOException::class)
    fun submit(file: MultipartFile, rule: String) {
        val excelParsingRule = excelParsingRules.first { e -> e.ruleName == rule }
        val fileName = "/tmp/" + Instant.now().epochSecond
        val tmpFile = File(fileName)
        file.transferTo(tmpFile)
        val fileParser = excelParsingRule.fileParser
        excelParsingRule.process(PoiImporter.processSheet(tmpFile, fileParser, excelParsingRule.entityClass))
    }
}