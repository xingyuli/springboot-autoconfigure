package com.mobisist.springbootseed.autoconfigure.baidupush

import com.mobisist.components.messaging.baidupush.IOSDeployStatus
import com.mobisist.springbootseed.autoconfigure.AbstractProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.baidupush")
class BaiduPushProperties : AbstractProperties() {

    // NOTE: null indicates outermost configuration is not defined
    var name: String? = null

    // TODO low-priority support default msgExpires

    var iosApiKey: String? = null
    @Transient var iosSecretKey: String? = null
    var iosDeployStatus: IOSDeployStatus = IOSDeployStatus.DEVELOPMENT

    var androidApiKey: String? = null
    @Transient var androidSecretKey: String? = null

    // multi-configurations support
    var configurations: List<BaiduPushProperties>? = null

    override fun afterPropertiesSet() {
        if (configurations == null || configurations!!.isEmpty()) {
            // force the outermost configuration name to 'default'
            name = DEFAULT_NAME

        } else {
            if (configurations!!.find { it.name.isNullOrBlank() } != null) {
                throw RuntimeException("name must be explicitly specified when using multi-configurations")
            }

            // any non-blank property indicates using outermost configuration
            if (arrayOf(iosApiKey, iosSecretKey, androidApiKey, androidSecretKey).find { !it.isNullOrBlank() } != null) {
                throw RuntimeException("you are mixing outermost and multi configurations, please choose either one but not both")
            }
        }
        super.afterPropertiesSet()
    }

    companion object {
        private const val DEFAULT_NAME = "default"
    }

}