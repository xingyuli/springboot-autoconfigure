package com.mobisist.springbootseed.autoconfigure.baidupush

import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest
import com.mobisist.components.messaging.baidupush.BaiduPushConfig
import com.mobisist.components.messaging.baidupush.BaiduPushMessage
import com.mobisist.components.messaging.baidupush.BaiduPushSender
import com.mobisist.springbootseed.autoconfigure.ConfigurationNotFoundException
import com.mobisist.springbootseed.autoconfigure.jsonStringify
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap

// TODO low-priority - separate bpush, yunpian and wechat push, so that the class sepcified by ConditionalOnClass and ConditionalOnMissingBean could match
@Configuration
@ConditionalOnClass(BaiduPushSender::class)
@ConditionalOnMissingBean(BaiduPushSender::class)
@EnableConfigurationProperties(BaiduPushProperties::class)
open class BaiduPushAutoConfiguration(private val properties: BaiduPushProperties) {

    @Bean
    open fun baiduPushSender(): BaiduPushSender = DefaultBaiduPushSender().apply {
        androidConfigProvider = { configName ->
            val matchingProperties = propertiesFor(configName)
            BaiduPushConfig.AndroidPushConfig().apply {
                apiKey = matchingProperties.androidApiKey!!
                secretKey = matchingProperties.androidSecretKey!!
            }
        }
        iosConfigProvider = { configName ->
            val matchingProperties = propertiesFor(configName)
            BaiduPushConfig.IOSPushConfig().apply {
                apiKey = matchingProperties.iosApiKey!!
                secretKey = matchingProperties.iosSecretKey!!
                deployStatus = matchingProperties.iosDeployStatus
            }
        }
    }

    private inner class DefaultBaiduPushSender : BaiduPushSender() {

        private val logger = LoggerFactory.getLogger(DefaultBaiduPushSender::class.java)

        private val propertiesCache = ConcurrentHashMap<String, BaiduPushProperties>()

        internal fun propertiesFor(config: String) = propertiesCache.getOrPut(config) {
            val matchingProperties = getPropertiesByConfigName(config)
            logger.debug("""you are viewing the properties as this is the first time accessing the config named '$config'
                |${matchingProperties.jsonStringify(prettyPrint = true)}""".trimMargin())
            matchingProperties
        }

        private fun getPropertiesByConfigName(configName: String): BaiduPushProperties {
            if (properties.name == configName) {
                return properties
            }
            return properties.configurations?.find { it.name == configName } ?: throw ConfigurationNotFoundException(BaiduPushSender::class.java, configName)
        }

        override fun send(msg: BaiduPushMessage) {
            if (propertiesFor(msg.config).enabled) {
                super.send(msg)

            } else {
                val req = msg.req
                when (req) {
                    is PushMsgToSingleDeviceRequest -> {
                        logger.debug("""inspecting BaiduPushMessage ...
                        |${req.jsonStringify(prettyPrint = true)}""".trimMargin())
                    }
                    else -> logger.warn("msg cannot be inspected as it is not a PushMsgToSingleDeviceRequest")
                }
            }
        }

    }

}
