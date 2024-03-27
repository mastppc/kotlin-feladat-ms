package hu.vanio.kotlin.feladat.ms.service

import hu.vanio.kotlin.feladat.ms.client.OpenMeteoClient
import hu.vanio.kotlin.feladat.ms.client.dto.ForecastValuesDto
import hu.vanio.kotlin.feladat.ms.exception.ForecastException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TreeMap
import java.util.stream.Collectors
import java.util.stream.Collectors.averagingDouble

@Service
class OpenMeteoService(
        @Autowired private val openMeteoClient: OpenMeteoClient) {
    fun forecast()  = openMeteoClient.forecast()

    fun getForecastTemperatures(forecastValues: ForecastValuesDto): Map<LocalDateTime, Double> {
        val result: MutableMap<LocalDateTime, Double> = HashMap()

        val timesIterator: Iterator<LocalDateTime> = forecastValues.times.iterator()
        val temperaturesIterator: Iterator<Double> = forecastValues.temperatures.iterator()

        while (timesIterator.hasNext() && temperaturesIterator.hasNext()) {
            result[timesIterator.next()] = temperaturesIterator.next()
        }

        if (timesIterator.hasNext() || temperaturesIterator.hasNext()) {
            throw ForecastException("Forecast values (times and temperatures) are inconsistent")
        }

        return result
    }

    fun dailyAverageTemperatureForecast(
            forecastTemperatures: Map<LocalDateTime, Double>): Map<LocalDate, Double> =
        forecastTemperatures.entries
            .stream()
            .collect(Collectors.groupingBy({ it.key.toLocalDate() }, ::TreeMap, averagingDouble { it.value }))

    fun averageTemperatureForecast(
            forecastTemperatures: Map<LocalDateTime, Double>,
            date: LocalDate) =
        forecastTemperatures.entries
            .stream()
            .filter { it.key.toLocalDate() == date }
            .mapToDouble { it.value }
            .average()
            .orElseThrow { ForecastException("No average temperature available on date '$date' in forecast temperatures") }
}