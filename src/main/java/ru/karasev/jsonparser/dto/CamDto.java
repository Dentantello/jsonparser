package ru.karasev.jsonparser.dto;

import lombok.Data;

@Data
public class CamDto {
    private int id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
