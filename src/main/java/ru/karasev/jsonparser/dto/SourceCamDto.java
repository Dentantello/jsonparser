package ru.karasev.jsonparser.dto;

import lombok.Data;
import ru.karasev.jsonparser.dto.enums.UrlType;

@Data
public class SourceCamDto {
    private UrlType urlType;
    private String videoUrl;
}
