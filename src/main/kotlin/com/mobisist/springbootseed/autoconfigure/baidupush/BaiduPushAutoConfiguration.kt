package com.mobisist.springbootseed.autoconfigure.baidupush

import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest
import com.mobisist.components.messaging.baidupush.BaiduPushConfig
import com.mobisist.components.messaging.baidupush.BaiduPushMessage
import com.mobisist.components.messaging.baidupush.BaiduPushSender
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// TODO low-priority - separate bpush, yunpian and wechat push, so that the class sepcified by ConditionalOnClass and ConditionalOnMissingBean could match
// TODO there might be multiple suites of baidu push properties, how to support that?
@Configuration
@ConditionalOnClass(BaiduPushSender::class)
@ConditionalOnMissingBean(BaiduPushSender::class)
@EnableConfigurationProperties(BaiduPushProperties::class)
open class BaiduPushAutoConfiguration(private val properties: BaiduPushProperties) {

    @Bean
    open fun baiduPushSender(): BaiduPushSender = if (properties.enabled!!) {
        BaiduPushSender().apply {
            androidConfig = BaiduPushConfig().apply {
                apiKey = properties.androidApiKey!!
                secretKey = properties.androidSecretKey!!
            }
            iosConfig = BaiduPushConfig().apply {
                apiKey = properties.iosApiKey!!
                secretKey = properties.iosSecretKey!!
            }
        }
    } else {
        DummyBaiduPushSender()
    }

    private class DummyBaiduPushSender : BaiduPushSender() {

        private val logger = LoggerFactory.getLogger(DummyBaiduPushSender::class.java)

        override fun send(msg: BaiduPushMessage) {
            val req = msg.req
            when (req) {
                is PushMsgToSingleDeviceRequest -> {
                    // TODO depends on com.mobisist.swordess.common
//                    logger.info("bpush is disabled, introspecting msg...\n${req.jsonStringify(prettyPrint = true)}")
                    logger.debug("introspecting msg...\nchannelId: ${req.channelId}, message: ${req.message}")
                }
                else -> logger.warn("msg cannot be introspected as it is not a PushMsgToSingleDeviceRequest")
            }
        }

    }

}
