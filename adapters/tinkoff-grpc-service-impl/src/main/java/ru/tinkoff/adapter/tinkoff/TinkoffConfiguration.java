package ru.tinkoff.adapter.tinkoff;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.ReplayProcessor;
import ru.tinkoff.adapter.instruments.InstrumentService;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
public class TinkoffConfiguration {

    @Bean
    @ConditionalOnProperty(name = "integration.tinkoff.listener.enabled", havingValue = "true")
    public InvestApi investApi(TinkoffConfigurationProperties tinkoffConfigurationProperties) {
        return tinkoffConfigurationProperties.isTokenSandbox
                ? InvestApi.createSandbox(tinkoffConfigurationProperties.tokenSandbox, tinkoffConfigurationProperties.getAppName())
                : InvestApi.create(tinkoffConfigurationProperties.tokenProm, tinkoffConfigurationProperties.getAppName());
    }

    @Bean("ReplayProcessorShares")
    @ConditionalOnProperty(name = "integration.tinkoff.shares.listener.enabled", havingValue = "true")
    public ReplayProcessor<Candle> publisherSharesAdapter(InvestApi investApi,
                                                          @Qualifier("InstrumentShares") InstrumentService instrumentService){
         return new PublisherSharesAdapter(investApi, instrumentService).start();
    }

    @Configuration
    @ConfigurationProperties(prefix = "integration.tinkoff")
    @Data
    public class TinkoffConfigurationProperties {
        private String emulator;
        private Boolean isTokenSandbox;
        private String tokenSandbox;
        private String tokenProm;
        private String accountId;
        private String appName;
    }

}

