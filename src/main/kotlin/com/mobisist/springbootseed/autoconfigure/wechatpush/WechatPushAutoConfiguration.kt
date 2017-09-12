package com.mobisist.springbootseed.autoconfigure.wechatpush

import com.mobisist.components.messaging.wechatpush.TemplateIdProvider
import com.mobisist.components.messaging.wechatpush.WechatPushMessage
import com.mobisist.components.messaging.wechatpush.WechatPushSender
import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.WxMpServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(WechatPushSender::class)
@ConditionalOnMissingBean(WechatPushSender::class)
@EnableConfigurationProperties(WechatPushProperties::class)
open class WechatPushAutoConfiguration(private val properties: WechatPushProperties) {

    @Bean
    @ConditionalOnMissingBean(TemplateIdProvider::class)
    open fun templateIdProvider(): TemplateIdProvider = DefaultTemplateIdProvider()

    @Bean
    @ConditionalOnMissingBean(WxMpService::class)
    open fun wxMpService(): WxMpService = WxMpServiceImpl().apply {
        setWxMpConfigStorage(WxMpInMemoryConfigStorage().apply {
            appId = properties.appId
            secret = properties.appSecret
            token = properties.token
            aesKey = properties.aesKey
        })
    }

    @Bean
    open fun wechatPushSender(): WechatPushSender = if (properties.enabled!!) {
        WechatPushSender().apply {
            templateIdProvider = templateIdProvider()
            wxMpService = wxMpService()
        }
    } else {
        DummyWechatPushSender().apply {
            templateIdProvider = templateIdProvider()
        }
    }

    private inner class DefaultTemplateIdProvider : TemplateIdProvider {
        override fun get(templateKey: String): String = properties.templates!![templateKey]!!
    }

    private class DummyWechatPushSender : WechatPushSender() {
        override fun send(msg: WechatPushMessage) {
            logger.debug("""send via dummy, use Template(key=${msg.templateKey},id=${templateIdProvider!!.get(msg.templateKey)}), send to ${msg.openId}, with {
            |    templateKey: ${msg.templateKey},
            |    templateData: ${msg.templateData}
            |}""".trimMargin())
        }
    }

}