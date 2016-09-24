package com.mbenabda.techwatch.testES.config.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DroolsConfiguration {

    @PostConstruct
    public void fireAllRules() {
        // load up the knowledge base
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        KieSession session = container.newKieSession("ksession-test");

        new LoadedRulesLogger().logRulesLoadedIn(session.getKieBase());

        session.fireAllRules();
    }
}
