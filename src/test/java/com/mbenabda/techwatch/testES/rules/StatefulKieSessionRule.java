package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.config.drools.LoadedRulesLogger;
import org.apache.commons.lang3.Validate;
import org.junit.rules.ExternalResource;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class StatefulKieSessionRule extends ExternalResource {

    private final String sessionName;
    private KieSession session;

    public StatefulKieSessionRule(String sessionName) {
        Validate.notNull(sessionName);
        this.sessionName = sessionName;
    }

    public StatefulKieSessionRule() {
        this("ksession-test");
    }

    @Override
    protected void before() throws Throwable {
        KieServices services = KieServices.Factory.get();
        KieContainer container = services.getKieClasspathContainer();
        this.session = container.newKieSession(sessionName);
        new LoadedRulesLogger().logRulesLoadedIn(session.getKieBase());
    }

    @Override
    protected void after() {
        session.destroy();
    }

    public KieSession session() {
        return session;
    }
}
