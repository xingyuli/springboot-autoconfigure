package com.mobisist.springbootseed.autoconfigure.wechatpush

import com.mobisist.components.messaging.wechatpush.TemplateIdProvider
import com.mobisist.components.messaging.wechatpush.WechatPushMessage
import com.mobisist.components.messaging.wechatpush.WechatPushSender
import com.mobisist.springbootseed.autoconfigure.jsonStringify
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.WxMpServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(WxMpService::class, WechatPushSender::class)
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
    open fun wechatPushSender(templateIdProvider: TemplateIdProvider, wxMpService: WxMpService): WechatPushSender {
        return if (properties.enabled) {
            WechatPushSender().apply {
                this.templateIdProvider = templateIdProvider
                this.wxMpService = wxMpService
            }
        } else {
            DummyWechatPushSender().apply {
                this.templateIdProvider = templateIdProvider
            }
        }
    }

    private inner class DefaultTemplateIdProvider : TemplateIdProvider {
        override fun get(templateKey: String): String = properties.templates!![templateKey]!!
    }

    private class DummyWechatPushSender : WechatPushSender() {

        private val logger = LoggerFactory.getLogger(DummyWechatPushSender::class.java)

        override fun send(msg: WechatPushMessage) {
            logger.debug("""inspecting WechatPushMessage ...
                |${msg.jsonStringify(prettyPrint = true)}""".trimMargin())
        }

    }

}