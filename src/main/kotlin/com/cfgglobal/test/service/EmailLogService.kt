package com.cfgglobal.test.service

import com.cfgglobal.test.dao.EmailLogDao
import com.cfgglobal.test.domain.EmailLog
import com.cfgglobal.test.service.base.BaseService
import io.vavr.API
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.persistence.criteria.Predicate

@Service
class EmailLogService : BaseService<EmailLog, Long>() {

    @Autowired
    private val emailLogDao: EmailLogDao? = null


    fun findForSend(customEmail: EmailLog): List<EmailLog> {
        return emailLogDao!!.findAll { root, query, cb ->
            var predicates: io.vavr.collection.List<Predicate> = API.List()
            if (customEmail.status != null) {
                val p = cb.equal(root.get<Any>("status"), customEmail.status)
                predicates = predicates.append(p)
            }
            if (customEmail.times != null) {
                val p2 = cb.lessThan(root.get("times"), customEmail.times)
                predicates = predicates.append(p2)
            }
            if (predicates.isEmpty) null else predicates.reduce({ x, y -> cb.and(x, y) })

        }
    }


    fun update(customEmailLog: EmailLog) {
        emailLogDao!!.save(customEmailLog)
    }


    operator fun get(id: String): EmailLog {
        return emailLogDao!!.findById(NumberUtils.createLong(id)!!).get()
    }

    operator fun get(id: Long?): EmailLog {
        return emailLogDao!!.findById(id!!).get()
    }
}
