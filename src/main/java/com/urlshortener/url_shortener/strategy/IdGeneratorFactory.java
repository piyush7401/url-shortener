package com.urlshortener.url_shortener.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IdGeneratorFactory {
    private Map<StrategyType, IdGenerationStrategy> StrategyMap;

    public  IdGeneratorFactory(List<IdGenerationStrategy> strategies) {
        StrategyMap = new HashMap<>();
        for(IdGenerationStrategy strategy : strategies) {
            StrategyType type = strategy.getStrategyType();
            StrategyMap.put(type, strategy);
        }
    }

    public IdGenerationStrategy getIdGenerator(StrategyType strategyType) {
        IdGenerationStrategy strategy =  StrategyMap.get(strategyType);
        if(strategy == null){
            throw new IllegalArgumentException("No strategy found for type " + strategyType);
        }
        return strategy;
    }
}
