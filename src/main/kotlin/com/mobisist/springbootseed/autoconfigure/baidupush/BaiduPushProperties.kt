package com.mobisist.springbootseed.autoconfigure.baidupush

import com.mobisist.components.messaging.baidupush.IOSDeployStatus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import com.mobisist.springbootseed.autoconfigure.DelegatedProperties
import com.sun.media.jfxmediaimpl.platform.ios.IOSPlatform

@ConfigurationProperties(prefix = "springbootseed.messaging.baidupush")
class BaiduPushProperties : DelegatedProperties() {

    var iosApiKey: String? by config
    var iosSecretKey: String? by config
    var iosDeployStatus: Int? by config

    var androidApiKey: String? by config
    var androidSecretKey: String? by config

    init {
        iosDeployStatus = IOSDeployStatus.DEVELOPMENT.intValue
    }

}