package com.example.application.service;

import com.example.application.dto.CityDto;
import com.example.application.dto.CountryDto;
import com.example.application.dto.StateDto;
import java.util.List;

public interface MasterDataService {
    List<CountryDto> getAllCountries();

    List<StateDto> getStatesByCountryId(Long countryId);

    List<CityDto> getCitiesByStateId(Long stateId);
}
