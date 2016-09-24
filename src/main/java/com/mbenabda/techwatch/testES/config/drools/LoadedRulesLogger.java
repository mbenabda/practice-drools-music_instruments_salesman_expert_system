package com.mbenabda.techwatch.testES.config.drools;

import org.kie.api.KieBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LoadedRulesLogger {
    private static final Logger LOG = LoggerFactory.getLogger(LoadedRulesLogger.class);

    public void logRulesLoadedIn(KieBase kieBase) {
        List<String> rules = kieBase.getKiePackages().stream()
            .flatMap(p -> p.getRules().stream())
            .map(r -> r.getName())
            .collect(toList());
        LOG.info("loaded rules : {}\n\n", String.join("\n", rules));
    }
}
