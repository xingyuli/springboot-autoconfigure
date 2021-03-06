package com.mobisist.springbootseed.autoconfigure.yunpian

import com.mobisist.springbootseed.autoconfigure.AbstractProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.yunpian")
class YunPianProperties : AbstractProperties() {

    @Transient
    var apiKey: String? = null

}