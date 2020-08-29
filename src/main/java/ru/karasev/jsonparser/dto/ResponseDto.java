package ru.karasev.jsonparser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseDto {
    private List<FullCamInfoDto> resultList;

    public ResponseDto(List<FullCamInfoDto> resultList) {
        this.resultList = resultList;
    }
}
