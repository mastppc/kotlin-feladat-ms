package hu.vanio.kotlin.feladat.ms

import hu.vanio.kotlin.feladat.ms.service.OpenMeteoService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import java.time.LocalDate

@SpringBootApplication
@EnableFeignClients
class WeatherApp: CommandLineRunner {
    @Autowired
    private lateinit var openMeteoService: OpenMeteoService

    override fun run(vararg args: String) {
        val forecast = openMeteoService.forecast()
        val forecastTemperatures = openMeteoService.getForecastTemperatures(forecast.values)

        if (args.isNotEmpty()) {
            val date = LocalDate.parse(args[0])

            printAverageTemperatureForecastOnDate(
                date,
                openMeteoService.averageTemperatureForecast(forecastTemperatures, date),
                forecast.units.temperature)
        } else {
            printDailyAverageTemperatureForecast(
                openMeteoService.dailyAverageTemperatureForecast(forecastTemperatures),
                forecast.units.temperature)
        }
    }

    fun printAverageTemperatureForecastOnDate(
            date: LocalDate,
            averageTemperature: Double,
            temperatureUnit: String) {
        LOG.info(DAILY_AVERAGE_TEMPERATURE_PATTERN, date, averageTemperature, temperatureUnit)
    }

    fun printDailyAverageTemperatureForecast(
            forecastDailyAverageTemperature: Map<LocalDate, Double>,
            temperatureUnit: String) {
        for (element in forecastDailyAverageTemperature) {
            LOG.info(DAILY_AVERAGE_TEMPERATURE_PATTERN, element.key, element.value, temperatureUnit)
        }
    }

    companion object {
        const val DAILY_AVERAGE_TEMPERATURE_PATTERN = "{}: {}{}"

        @JvmStatic
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val LOG = LoggerFactory.getLogger(javaClass.enclosingClass)
    }
}

fun main(args: Array<String>) {
    runApplication<WeatherApp>(*args.map { it }.toTypedArray())
}