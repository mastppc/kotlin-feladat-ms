package hu.vanio.kotlin.feladat.ms.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ForecastUnitsDto(
        @JsonProperty("temperature_2m")
        val temperature: String)