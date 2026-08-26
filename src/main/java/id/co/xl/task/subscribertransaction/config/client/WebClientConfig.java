package id.co.xl.task.subscribertransaction.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${getpin.baseurl}")
    private String getPinBaseUrl;

    @Bean
    public WebClient genericWebClient() {
        return WebClient.builder()
                .baseUrl(getPinBaseUrl)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE
                ).build();
    }
}
