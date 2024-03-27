package hu.vanio.kotlin.feladat.ms.service

import hu.vanio.kotlin.feladat.ms.client.OpenMeteoClient
import hu.vanio.kotlin.feladat.ms.client.dto.ForecastValuesDto
import hu.vanio.kotlin.feladat.ms.exception.ForecastException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class OpenMeteoServiceTest {
    private lateinit var openMeteoService: OpenMeteoService

    @Mock
    private lateinit var openMeteoClientMock: OpenMeteoClient

    @BeforeEach
    fun setUp() {
        openMeteoService = OpenMeteoService(openMeteoClientMock)
    }

    @Test
    fun getForecastTemperatures() {
        // Given
        val forecastValues = ForecastValuesDto(
            listOf(TIME_1, TIME_2, TIME_3),
            listOf(TEMPERATURE_1, TEMPERATURE_2, TEMPERATURE_3))

        // When
        val forecastTemperatures = openMeteoService.getForecastTemperatures(forecastValues)

        // Then
        assertEquals(
            mapOf(TIME_1 to TEMPERATURE_1, TIME_2 to TEMPERATURE_2, TIME_3 to TEMPERATURE_3),
            forecastTemperatures)
    }

    @Test
    fun getForecastTemperatures_inconsistentValues() {
        // Given
        val forecastValues = ForecastValuesDto(
            listOf(TIME_1),
            listOf(TEMPERATURE_1, TEMPERATURE_2))

        // When - then
        val exception = assertThrows<ForecastException> { openMeteoService.getForecastTemperatures(forecastValues) }

        assertEquals("Forecast values (times and temperatures) are inconsistent", exception.message)
    }

    @Test
    fun dailyAverageTemperatureForecast() {
        // Given
        val forecastTemperatures = mapOf(TIME_1 to TEMPERATURE_1, TIME_2 to TEMPERATURE_2, TIME_3 to TEMPERATURE_3)

        // When
        val dailyAverageTemperatureForecast =
            openMeteoService.dailyAverageTemperatureForecast(forecastTemperatures)

        // Then
        assertEquals(
            mapOf(
                LocalDate.of(2024, 1, 1) to 15.0,
                LocalDate.of(2024, 1, 2) to 20.0),
            dailyAverageTemperatureForecast)
    }

    @ParameterizedTest
    @MethodSource
    fun averageTemperatureForecast(
            date: LocalDate,
            expectedAverageTemperatureForecast: Double) {
        // Given
        val forecastTemperatures = mapOf(TIME_1 to TEMPERATURE_1, TIME_2 to TEMPERATURE_2, TIME_3 to TEMPERATURE_3)

        // When
        val averageTemperatureForecast = openMeteoService.averageTemperatureForecast(
            forecastTemperatures,
            date)

        // Then
        assertEquals(expectedAverageTemperatureForecast, averageTemperatureForecast)
    }

    companion object {
        val TIME_1: LocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0)
        val TIME_2: LocalDateTime = LocalDateTime.of(2024, 1, 1, 1, 0)
        val TIME_3: LocalDateTime = LocalDateTime.of(2024, 1, 2, 0, 0)

        const val TEMPERATURE_1 = 14.0
        const val TEMPERATURE_2 = 16.0
        const val TEMPERATURE_3 = 20.0

        @JvmStatic
        fun averageTemperatureForecast() = listOf(
            Arguments.of(LocalDate.of(2024, 1, 1), 15.0),
            Arguments.of(LocalDate.of(2024, 1, 2), 20.0))
    }
}