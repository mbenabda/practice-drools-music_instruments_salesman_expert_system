package com.mbenabda.techwatch.testES.config.drools;

import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.instruments.CharacteristicInstrument;
import com.mbenabda.techwatch.testES.facts.noise.LoudnessThreshold;
import com.mbenabda.techwatch.testES.repository.GenreRepository;
import com.mbenabda.techwatch.testES.repository.InstrumentRepository;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DroolsConfiguration {
    @Autowired
    FakeDataInitializer fakeDataInitializer;

    @Autowired
    InitialFactsInitializer initialFactsInitializer;

    // load up the knowledge base
    KieSession session;
    @Bean
    public KieSession kieSession() {
        if(session == null) {
            KieServices services = KieServices.Factory.get();
            KieContainer container = services.getKieClasspathContainer();
            session = container.newKieSession("ksession-test");

            new LoadedRulesLogger().logRulesLoadedIn(session.getKieBase());


        }
        return session;
    }

    @PostConstruct
    public void fireAllRules() {
        populateDatabase();
        addInitialFacts();

        kieSession().fireAllRules();
    }

    private void addInitialFacts() {
        initialFactsInitializer.init();
    }

    private void populateDatabase() {
        fakeDataInitializer.init();
    }
}
