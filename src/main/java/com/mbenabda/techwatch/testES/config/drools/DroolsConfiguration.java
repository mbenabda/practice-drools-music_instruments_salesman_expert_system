package com.mbenabda.techwatch.testES.config.drools;

import com.mbenabda.techwatch.testES.facts.AlwaysTrue;
import org.kie.api.cdi.KContainer;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.spring.annotations.KModuleAnnotationPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Configuration
@ImportResource(
    locations = {
        "classpath:kmodule.xml"
    }
)
public class DroolsConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DroolsConfiguration.class);

    @KContainer
    KieContainer container;

    @KSession("ksession-test")
    KieSession session;

    @Bean
    static KModuleAnnotationPostProcessor kieAnnotationsProcessor() {
        return new KModuleAnnotationPostProcessor();
    }

    @Bean(name = "firedRulesLoggingListener")
    FiredRulesLoggingListener firedRulesLoggingListener() {
        return new FiredRulesLoggingListener();
    }

    @PostConstruct
    void fireAllRules() {
        new LoadedRulesLogger().logRulesLoadedIn(container, LOG);
        session.insert(new AlwaysTrue());
        session.fireAllRules();
    }
}
