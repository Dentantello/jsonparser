package ru.karasev.jsonparser.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.karasev.jsonparser.dto.CamDto;
import ru.karasev.jsonparser.dto.SourceCamDto;
import ru.karasev.jsonparser.dto.TokenCamDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomCamSourceService implements CamSourceService{

    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    @Value("${cams-url}")
    private String mockCamsUrl;

    @Override
    public List<CamDto> getAvailableCams() {
        return proceedRequest(new TypeReference<List<CamDto>> () {
        }, mockCamsUrl);
    }

    @Override
    @Async
    public CompletableFuture<SourceCamDto> getSourceCamData(String sourceDataUrl) {
        return CompletableFuture.completedFuture(proceedRequest(new TypeReference<SourceCamDto>() {
        }, sourceDataUrl));
    }

    @Override
    @Async
    public CompletableFuture<TokenCamDto> getTokenCamData(String tokenDataUrl) {
        return CompletableFuture.completedFuture(proceedRequest(new TypeReference<TokenCamDto>() {
        }, tokenDataUrl));
    }

    @SneakyThrows
    private <T> T proceedRequest(TypeReference<T> responseClassTypeReference, String url) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url);
        log.info("Request to {}", builder.toUriString());
        HttpEntity<?> entity = new HttpEntity<>(null);
        T responseDto;
        String body = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
        log.info("Response {}", body);
        assert body != null;
        responseDto = mapper.readValue(body, responseClassTypeReference);
        return responseDto;
    }
}
