package com.migu.tsg.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import com.netflix.discovery.EurekaClientConfig;

/**
 * 
 * @author zhangq
 *
 */
@Configuration
@ConditionalOnClass(EurekaClientConfig.class)
@AutoConfigureBefore(EurekaClientAutoConfiguration.class)
public class EurekaAutoConfiguration {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    private RelaxedPropertyResolver propertyResolver;

    public EurekaAutoConfiguration(ConfigurableEnvironment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env);
    }

    @Bean
    @ConditionalOnMissingBean(value = EurekaClientConfig.class, search = SearchStrategy.CURRENT)
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean client = new HeadlessServiceEurekaClientConfigBean();

        logger.info("My custom EurekaClientConfigBean created.");
        
        // super:
        if ("bootstrap".equals(propertyResolver.getProperty("spring.config.name"))) {
        		logger.info("EurekaClientConfigBean setRegisterWithEureka() to FALSE.");
            client.setRegisterWithEureka(false);
        }
        
        return client;
    }
}