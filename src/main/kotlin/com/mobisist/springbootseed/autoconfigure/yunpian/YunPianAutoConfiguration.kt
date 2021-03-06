package com.mobisist.springbootseed.autoconfigure.yunpian

import com.mobisist.components.messaging.sms.SmsMessage
import com.mobisist.components.messaging.sms.yunpian.v1.YunPianConfig
import com.mobisist.components.messaging.sms.yunpian.v1.YunPianResponse
import com.mobisist.components.messaging.sms.yunpian.v1.YunPianSender
import com.mobisist.springbootseed.autoconfigure.jsonStringify
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// TODO low-priority - add property for specifying yunpian version?
// TODO define yunpian java api and check that class
@Configuration
@ConditionalOnClass(YunPianSender::class)
@ConditionalOnMissingBean(YunPianSender::class)
@EnableConfigurationProperties(YunPianProperties::class)
open class YunPianAutoConfiguration(private val properties: YunPianProperties) {

    @Bean
    open fun yunPianSender(): YunPianSender = if (properties.enabled) {
        YunPianSender().apply {
            config = YunPianConfig().apply {
                apikey = properties.apiKey!!
            }
        }
    } else {
        DummyYunPianSender()
    }

    private class DummyYunPianSender : YunPianSender() {

        private val logger = LoggerFactory.getLogger(DummyYunPianSender::class.java)

        override fun send(msg: SmsMessage): YunPianResponse {
            logger.debug("""inspecting SmsMessage and return success response directly ...
                |${msg.jsonStringify(prettyPrint = true)}""".trimMargin())
            return YunPianResponse(mapOf("code" to 0, "msg" to "success"))
        }

    }

}