package com.example.application.service.impl;

import com.example.application.dto.CityDto;
import com.example.application.dto.CountryDto;
import com.example.application.dto.StateDto;
import com.example.application.entity.City;
import com.example.application.entity.Country;
import com.example.application.entity.State;
import com.example.application.repository.CityRepository;
import com.example.application.repository.CountryRepository;
import com.example.application.repository.StateRepository;
import com.example.application.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService {

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    @Override
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::mapToCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StateDto> getStatesByCountryId(Long countryId) {
        return stateRepository.findByCountryId(countryId).stream()
                .map(this::mapToStateDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getCitiesByStateId(Long stateId) {
        return cityRepository.findByStateId(stateId).stream()
                .map(this::mapToCityDto)
                .collect(Collectors.toList());
    }

    private CountryDto mapToCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    private StateDto mapToStateDto(State state) {
        StateDto dto = new StateDto();
        dto.setId(state.getId());
        dto.setName(state.getName());
        dto.setCountryId(state.getCountry().getId());
        return dto;
    }

    private CityDto mapToCityDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setStateId(city.getState().getId());
        return dto;
    }
}
