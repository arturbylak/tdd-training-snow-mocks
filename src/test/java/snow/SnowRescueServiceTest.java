package snow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.SnowplowMalfunctioningException;
import snow.dependencies.WeatherForecastService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SnowRescueServiceTest {
    private SnowRescueService snowRescueService;
    private WeatherForecastService weatherForecastService;
    private MunicipalServices municipalServices;
    private PressService pressService;

    @Before
    public void setUp() {
        weatherForecastService = mock(WeatherForecastService.class);
        pressService = mock(PressService.class);
        municipalServices = mock(MunicipalServices.class);

        snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
    }

    @Test
    public void shouldSendSanderBecauseOfTemperatureLessThan0() {
        //given
        int temperatureInCelsius = -5;

        when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(temperatureInCelsius);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices).sendSander();
    }

    @Test
    public void shouldNotSendSanderBecauseOfTemperatureMoreThan0Celsius() {
        //given
        int temperatureInCelsius = 3;

        when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(temperatureInCelsius);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices, never()).sendSander();
    }

    @Test
    public void shouldNotSendSanderBecauseOfTemperatureEquals0Celsius() {
        //given
        int temperatureInCelsius = 0;

        when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(temperatureInCelsius);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices, never()).sendSander();
    }

    @Test
    public void shouldSendSnowplowBecauseOfSnowFallHeightEquals4() {
        //given
        int snowFallHeightInMM = 4;

        when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(snowFallHeightInMM);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices).sendSnowplow();
    }

    @Test
    public void shouldNotSendSnowplowBecauseOfSnowFallHeightEquals2() {
        //given
        int snowFallHeightInMM = 2;

        when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(snowFallHeightInMM);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices, never()).sendSnowplow();
    }

    @Test
    public void shouldNotSendSnowplowBecauseOfSnowFallHeightEquals3() {
        //given
        int snowFallHeightInMM = 3;

        when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(snowFallHeightInMM);

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices, never()).sendSnowplow();
    }

    @Test
    public void shouldSendNewSnowplowBecauseOfSnowplowMalfunctioningException() {
        //given
        int snowFallHeightInMM = 4;

        when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(snowFallHeightInMM);
        doThrow(SnowplowMalfunctioningException.class).doNothing().when(municipalServices).sendSnowplow();

        //when
        snowRescueService.checkForecastAndRescue();

        //then
        verify(municipalServices, times(2)).sendSnowplow();
    }
}
