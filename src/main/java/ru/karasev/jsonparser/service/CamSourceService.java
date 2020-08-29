package ru.karasev.jsonparser.service;

import ru.karasev.jsonparser.dto.CamDto;
import ru.karasev.jsonparser.dto.SourceCamDto;
import ru.karasev.jsonparser.dto.TokenCamDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CamSourceService {
    List<CamDto> getAvailableCams();

    CompletableFuture<SourceCamDto> getSourceCamData(String sourceDataUrl);

    CompletableFuture<TokenCamDto> getTokenCamData(String tokenDataUrl);
}
