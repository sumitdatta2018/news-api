package com.mydomain.newsapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@JsonInclude(Include.NON_NULL)
public class SearchCriteria {

  @Schema(type = "string", defaultValue = "hours",
      allowableValues = {"minutes", "hours", "days", "weeks", "months", "years"})
  @Default
  private ChronoUnit groupingInterval = ChronoUnit.HOURS;
  @Schema(defaultValue = "12")
  @Default
  private Integer groupingDuration = 12;
  @NotNull
  private String searchKeyword;
  @Default
  private Integer pageSize = 10;
  @Default
  private Integer pageNumber = 1;
}
