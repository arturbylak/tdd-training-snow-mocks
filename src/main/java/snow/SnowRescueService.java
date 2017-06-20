package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {
    private final WeatherForecastService weatherForecastService;
    private final MunicipalServices municipalServices;
    private final PressService pressService;

    public SnowRescueService(WeatherForecastService weatherForecastService, MunicipalServices municipalServices, PressService pressService) {
        this.weatherForecastService = weatherForecastService;
        this.municipalServices = municipalServices;
        this.pressService = pressService;
    }

    public void checkForecastAndRescue() {
        int averageTemperatureInCelsius = weatherForecastService.getAverageTemperatureInCelsius();

        if (averageTemperatureInCelsius < 0) {
            municipalServices.sendSander();
        }
    }
}
