package com.mobisist.springbootseed.autoconfigure.yunpian

import com.mobisist.springbootseed.autoconfigure.DelegatedProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.yunpian")
class YunPianProperties : DelegatedProperties() {

    var apiKey: String? by config

    init {
        sensitiveKeys.add("apiKey")
    }

}