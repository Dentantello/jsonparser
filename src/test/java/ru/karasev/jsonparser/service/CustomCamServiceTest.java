package ru.karasev.jsonparser.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.karasev.jsonparser.dto.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CustomCamService.class})
public class CustomCamServiceTest {


    @Autowired
    private CamService camService;

    @MockBean
    private CamSourceService camSourceService;

    @Autowired
    private ResourceLoader resourceLoader;

    private ObjectMapper mapper = new ObjectMapper();

    @Test(expected = Exception.class)
    public void shouldThrownExceptionWhenSourceCamDataFetchFailed() throws IOException {
        List<CamDto> camDtos = loadResource("mocky-io-main.json", new TypeReference<List<CamDto>>() {
        });
        doReturn(camDtos).when(camSourceService).getAvailableCams();
        doThrow(Exception.class).when(camSourceService).getSourceCamData(anyString());
        camService.collectCamSource();
    }

    @Test
    public void shouldReturnRightAnswer() throws IOException {
        List<CamDto> camDtos = loadResource("mocky-io-main.json", new TypeReference<List<CamDto>>() {
        });
        SourceCamDto sourceCamDto = loadResource("mocky-source-cam-data.json", new TypeReference<SourceCamDto>() {
        });
        TokenCamDto tokenCamDto = loadResource("mocky-token-cam-data.json", new TypeReference<TokenCamDto>() {
        });
        List<FullCamInfoDto> resultList = loadResource("mocky-right-answer.json", new TypeReference<List<FullCamInfoDto>>() {
        });

        doReturn(camDtos).when(camSourceService).getAvailableCams();
        doReturn(CompletableFuture.completedFuture(sourceCamDto)).when(camSourceService).getSourceCamData(anyString());
        doReturn(CompletableFuture.completedFuture(tokenCamDto)).when(camSourceService).getTokenCamData(anyString());

        ResponseDto responseDto = camService.collectCamSource();
        Assert.assertEquals(resultList, responseDto.getResultList());
    }

    private String loadResource(String resourceName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceName);
        StringBuilder stringBuilder = new StringBuilder();
        Files.lines(Paths.get(resource.getURI())).forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    private <T> T loadResource(String resourceName, TypeReference<T> clazz) throws IOException {
        String content = loadResource(resourceName);
        return mapper.readValue(content, clazz);
    }
}