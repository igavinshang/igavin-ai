package org.igavin.ai.springai.functioncalling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;


@Configuration
 public class WeatherAutoConfiguration {

    @Bean
    @Description("Use api.weather to get weather information.")
    public WeatherService getWeatherServiceFunction() {
        return new WeatherService();
    }

}