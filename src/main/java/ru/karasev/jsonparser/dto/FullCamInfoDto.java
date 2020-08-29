package ru.karasev.jsonparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.karasev.jsonparser.dto.enums.UrlType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCamInfoDto {
    private long id;
    private UrlType urlType;
    private String videoUrl;
    private String value;
    private int ttl;
}
