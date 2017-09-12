package com.mobisist.springbootseed.autoconfigure.baidupush

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import com.mobisist.springbootseed.autoconfigure.DelegatedProperties

@ConfigurationProperties(prefix = "springbootseed.messaging.baidupush")
class BaiduPushProperties : DelegatedProperties() {

    var iosApiKey: String? by config
    var iosSecretKey: String? by config
    var iosDeployStatus: Int? by config

    var androidApiKey: String? by config
    var androidSecretKey: String? by config

    init {
        // TODO low-priority - add enum: IosDeployStatus { DEVELOPMENT, PRODUCT }
        iosDeployStatus = 1 // 1:开发状态 2:生产状态
    }

}