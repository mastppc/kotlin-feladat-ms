package hu.vanio.kotlin.feladat.ms.client.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ForecastValuesDto(
        @JsonProperty("time")
        val times: List<LocalDateTime>,

        @JsonProperty("temperature_2m")
        val temperatures: List<Double>)