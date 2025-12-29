package com.example.application.controller;

import com.example.application.dto.CityDto;
import com.example.application.dto.CountryDto;
import com.example.application.dto.StateDto;
import com.example.application.dto.response.ApiResponse;
import com.example.application.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;

    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<CountryDto>>> getAllCountries() {
        List<CountryDto> countries = masterDataService.getAllCountries();
        return ResponseEntity.ok(new ApiResponse<>(true, "Countries fetched successfully", countries));
    }

    @GetMapping("/states/{countryId}")
    public ResponseEntity<ApiResponse<List<StateDto>>> getStatesByCountry(@PathVariable Long countryId) {
        List<StateDto> states = masterDataService.getStatesByCountryId(countryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "States fetched successfully", states));
    }

    @GetMapping("/cities/{stateId}")
    public ResponseEntity<ApiResponse<List<CityDto>>> getCitiesByState(@PathVariable Long stateId) {
        List<CityDto> cities = masterDataService.getCitiesByStateId(stateId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cities fetched successfully", cities));
    }
}
