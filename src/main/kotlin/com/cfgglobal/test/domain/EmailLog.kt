package com.cfgglobal.test.domain

import com.cfgglobal.test.enums.TaskStatus
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.*


@Entity
@DynamicUpdate
@DynamicInsert

data class EmailLog(

        val subject: String? = null,
        @Lob
        @Column(length = 100000)
        @JsonIgnore
        val content: ByteArray? = null,
        val sendTo: String? = null,
        @Enumerated(value = EnumType.STRING)
        val status: TaskStatus? = null,
        val times: Int? = null,
        val msg: String? = null
) : BaseEntity(), Serializable