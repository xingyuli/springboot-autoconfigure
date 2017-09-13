package com.mobisist.springbootseed.autoconfigure

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

open class DelegatedProperties : InitializingBean {

    private val logger = LoggerFactory.getLogger(javaClass)

    protected val config = mutableMapOf<String, Any?>().withDefault { null }

    protected val sensitiveKeys = mutableListOf<String>()

    var enabled: Boolean? by config

    // default values
    init {
        enabled = true
    }

    override fun afterPropertiesSet() {
        // TODO depends on com.mobisist.swordess.common
//        logger.info("messaging configurations are: ${config.jsonStringify(prettyPrint = true)}")

        // do not expose sensitive information to logs
        val insensitiveConfig = config.filter { !sensitiveKeys.contains(it.key) }
        logger.debug("""inspecting properties ...
            |$insensitiveConfig""".trimMargin())
    }

}