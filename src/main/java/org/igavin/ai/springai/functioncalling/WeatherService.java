package org.igavin.ai.springai.functioncalling;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Override
    public Response apply(Request request) {
        if (request == null || !StringUtils.hasText(request.city())) {
            logger.error("Invalid request: city is required.");
            return null;
        }
        try {
            Response response = new Response("beijing",
                    Map.of("temp_c", 50, "condition",
                            Map.of("text", "Sunny")
                    ),
                    List.of(Map.of("date", "2023-10-01", "day",
                                    Map.of("maxtemp_c", 70, "mintemp_c", 50)
                            )
                    )
            );

            logger.info("Weather data fetched successfully for city: {}", response.city());
            return response;
        } catch (Exception e) {
            logger.error("Failed to fetch weather data: {}", e.getMessage());
            return null;
        }
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Weather Service API request")
    public record Request(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("THE CITY OF INQUIRY") String city,

            @JsonProperty(required = false,
                    value = "days") @JsonPropertyDescription("The number of days for which the weather is forecasted") int days) {
    }

    @JsonClassDescription("Weather Service API response")
    public record Response(String city, Map<String, Object> current, List<Map<String, Object>> forecastDays) {
    }

}