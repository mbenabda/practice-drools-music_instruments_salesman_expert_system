package com.mbenabda.techwatch.testES.config.drools;

import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LoadedRulesLogger {

    public void logRulesLoadedIn(KieContainer container, Logger logger) {
        container.getKieBaseNames().stream()
            .map(container::getKieBase)
            .forEachOrdered(kieBase -> {
                logRulesLoadedIn(kieBase, logger);
            });
    }

    private void logRulesLoadedIn(KieBase kieBase, Logger log) {
        List<String> rules = kieBase.getKiePackages().stream()
            .map(p -> p.getRules())
            .flatMap(r -> r.stream())
            .map(r -> r.getName())
            .collect(toList());
        log.info("Loaded rules : " + String.join("\n", rules) + "\n\n");
    }
}
