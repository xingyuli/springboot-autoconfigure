package com.mobisist.springbootseed.autoconfigure

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

open class AbstractProperties : InitializingBean {

    @Transient protected val logger = LoggerFactory.getLogger(javaClass)

    var enabled: Boolean = false

    override fun afterPropertiesSet() {
        logger.debug("""inspecting properties ...
            |${this.jsonStringify(prettyPrint = true)}""".trimMargin())
    }

}

class ConfigurationNotFoundException(val usedBy: Class<*>, val name: String) : RuntimeException("configuration named '$name' for ${usedBy.name}")
