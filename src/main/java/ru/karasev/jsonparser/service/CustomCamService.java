package ru.karasev.jsonparser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.karasev.jsonparser.dto.CamDto;
import ru.karasev.jsonparser.dto.FullCamInfoDto;
import ru.karasev.jsonparser.dto.ResponseDto;
import ru.karasev.jsonparser.dto.SourceCamDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomCamService implements CamService{
    
    private final CamSourceService camSourceService;
    
    @Override
    public ResponseDto collectCamSource() {
        List<CamDto> availableCams = camSourceService.getAvailableCams();
        List<CompletableFuture<FullCamInfoDto>> completableFutures = new ArrayList<>();

        for (CamDto cam : availableCams) {
            completableFutures.add(fullCamInfo(cam));
        }

        return new ResponseDto(
                completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );
    }

    private CompletableFuture<FullCamInfoDto> fullCamInfo(CamDto cam) {
        CompletableFuture<SourceCamDto> camSourceInfo = camSourceService.getSourceCamData(cam.getSourceDataUrl());
        return camSourceInfo.thenCombine(camSourceService.getTokenCamData(cam.getTokenDataUrl()),
                (a, b) -> new FullCamInfoDto(
                        cam.getId(),
                        a.getUrlType(),
                        a.getVideoUrl(),
                        b.getValue(),
                        b.getTtl()
                ));
    }
}
