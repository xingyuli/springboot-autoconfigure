package com.mobisist.springbootseed.autoconfigure.wechatpush

import com.mobisist.springbootseed.autoconfigure.DelegatedProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.wechatpush")
class WechatPushProperties : DelegatedProperties() {

    var appId: String? by config
    var appSecret: String? by config

    // for receiving messages and events
    var token: String? by config
    var aesKey: String? by config

    // map template key to template id
    var templates: Map<String, String>? by config

    init {
        sensitiveKeys.add("appSecret")
        sensitiveKeys.add("token")
        sensitiveKeys.add("aesKey")
    }

}