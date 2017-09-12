package com.mobisist.springbootseed.autoconfigure.yunpian

import org.springframework.boot.context.properties.ConfigurationProperties
import com.mobisist.springbootseed.autoconfigure.DelegatedProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.yunpian")
class YunPianProperties : DelegatedProperties() {

    var apiKey: String? by config

}