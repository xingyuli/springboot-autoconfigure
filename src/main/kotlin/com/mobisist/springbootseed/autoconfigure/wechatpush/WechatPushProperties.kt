package com.mobisist.springbootseed.autoconfigure.wechatpush

import com.mobisist.springbootseed.autoconfigure.AbstractProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.wechatpush")
class WechatPushProperties : AbstractProperties() {

    var appId: String? = null
    @Transient var appSecret: String? = null

    // for receiving messages and events
    @Transient var token: String? = null
    @Transient var aesKey: String? = null

    // map template key to template id
    var templates: Map<String, String>? = null

}