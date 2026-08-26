package id.co.xl.task.subscribertransaction.service;

import id.co.xl.task.subscribertransaction.model.response.GetPinRs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class PinService {
    @Autowired
    private WebClient genericWebClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetPinRs getPin(String msisdn) {
        GetPinRs getPinRs = new GetPinRs().setStatus("error");
        String uri = "/subscriber/" + msisdn + "/pin";
        try {
            ResponseEntity<GetPinRs> getPinRsResponseEntity = this.genericWebClient.get()
                    .uri(uri).retrieve()
                    .toEntity(GetPinRs.class)
                    .block();

            log.info("[GET HTTP RESPONSE - SUCCESS][{}][{}][{}]", uri, getPinRsResponseEntity.getStatusCode(),
                    getPinRsResponseEntity.getBody());
            getPinRs = getPinRsResponseEntity.getBody();
        } catch (WebClientResponseException ex) {
            log.info("[GET HTTP RESPONSE - FAILED][{}][{}][{}]", uri, ex.getStatusCode(), ex.getResponseBodyAsString());
            try {
                // Upstream error body already matches our GetPinRs shape
                // (status/code/message/data),
                // so parse it directly instead of returning an empty "error" object.
                getPinRs = objectMapper.readValue(ex.getResponseBodyAsString(), GetPinRs.class);
            } catch (Exception parseEx) {
                log.info("[FAILED TO PARSE ERROR BODY][{}][{}]", uri, parseEx.getMessage());
                getPinRs = new GetPinRs()
                        .setStatus("failed")
                        .setCode(String.valueOf(ex.getStatusCode().value()))
                        .setMessage(ex.getMessage());
            }
        }
        return getPinRs;
    }
}
