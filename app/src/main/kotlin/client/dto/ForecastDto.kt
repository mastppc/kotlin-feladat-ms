package hu.vanio.kotlin.feladat.ms.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ForecastDto(
        @JsonProperty("hourly_units")
        val units: ForecastUnitsDto,

        @JsonProperty("hourly")
        val values: ForecastValuesDto)