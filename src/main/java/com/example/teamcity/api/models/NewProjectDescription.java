package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewProjectDescription {
    private Project parentProject;
    private String name;
    private String id;
    private Boolean copyAllAssociatedSettings;
    private String locator;
}
