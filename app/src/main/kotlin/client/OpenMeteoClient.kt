package hu.vanio.kotlin.feladat.ms.client

import hu.vanio.kotlin.feladat.ms.client.dto.ForecastDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "openMeteo", url = "\${open-meteo.url}")
interface OpenMeteoClient {
    @GetMapping(
        "/forecast?latitude=47.4984&longitude=19.0404&hourly=temperature_2m&timezone=auto",
        produces = [ MediaType.APPLICATION_JSON_VALUE ])
    fun forecast(): ForecastDto
}