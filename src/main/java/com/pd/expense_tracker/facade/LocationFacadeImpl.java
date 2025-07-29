package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.LocationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LocationFacadeImpl {

    private final RestTemplate restTemplate = new RestTemplate();

    public LocationDTO getLocation(String ipAddress) {
        String url = "https://ipapi.co/" + ipAddress + "/json/";
        Map response = restTemplate.getForObject(url, Map.class);

        return new LocationDTO(
                ipAddress,
                (String) response.get("city"),
                (String) response.get("country_name"),
                Double.parseDouble(response.get("latitude").toString()),
                Double.parseDouble(response.get("longitude").toString())
        );
    }
}
