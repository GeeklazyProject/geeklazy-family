package com.geeklazy.frame.starter.spectrum.listener;

import com.geeklazy.frame.common.utils.EmptyUtils;
import com.geeklazy.frame.common.utils.SysEnvUtils;
import com.google.common.collect.Lists;
import com.shuyun.spectrum.client.common.SettingsUtils;
import com.shuyun.spectrum.configuration.api.ConfigurationManager;
import com.shuyun.spectrum.configuration.api.ConfigurationManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;



/**
 * @Author geeklazy@163.com
 * @Date 2023/11/24 11:46
 * @Description
 */
@Slf4j
public class SpectrumAutoConfigurationRunListener implements SpringApplicationRunListener {
    private final SpringApplication application;
    private final String[] args;

    public SpectrumAutoConfigurationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        log.info("配置中心客户端初始化开始");
        String servers = SettingsUtils.getServers(SysEnvUtils.toMap(System.getProperties()));
        log.info(servers);
        if (EmptyUtils.isNotEmpty(servers)) {

            ConfigurationManager configurationManager = ConfigurationManagerFactory.create(map -> Lists.newArrayList(getConfigPath(SysEnvUtils.toMap(System.getProperties()), null)));
            configurationManager.startup();
        }
    }
    private static List<String> getConfigPath(Map<String, String> properties, String extPath) {
        String spectrumServer = SettingsUtils.getServers(Collections.EMPTY_MAP);
        if (null == spectrumServer) {
            System.setProperty("system.config.address", "http://just.for.use.spectrum.client.to.load.local.properties.file");
            System.setProperty("spectrum.key", "notUse");
            System.setProperty("spectrum.secret", "notUse");
        }

        String environment = SysEnvUtils.getSysOrEnv("ENVIRONMENT", properties.get("ENVIRONMENT"));
        if (StringUtils.isEmpty(environment)) {
            environment = SysEnvUtils.getSysOrEnv("system.environment", properties.get("system.environment"));
        }

        String serviceName = SysEnvUtils.getSysOrEnv("SERVICE_NAME", properties.get("SERVICE_NAME"));
        String appVersion = SysEnvUtils.getSysOrEnv("APP_VERSION", properties.get("APP_VERSION"));
        if (!StringUtils.isEmpty(environment) && !StringUtils.isEmpty(serviceName) && !StringUtils.isEmpty(appVersion)) {
            StringBuilder s = new StringBuilder();
            s.append('/').append(environment).append('/').append(serviceName).append('/').append(appVersion);
            s.append(",/").append(serviceName).append('/').append(appVersion);
            if (null != extPath && !extPath.isEmpty()) {
                s.append(",").append(extPath);
            }

            return Arrays.asList(s.toString().split(","));
        } else {
            throw new RuntimeException(String.format("some base configuration not found, system.environment = %s, SERVICE_NAME = %s, APP_VERSION = %s", environment, serviceName, appVersion));
        }
    }
}
