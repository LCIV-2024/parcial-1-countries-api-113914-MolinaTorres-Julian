package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.CountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<CountryDTO> getAllCountries() {
        return countryService.getAllCountriesDTO();
    }

    @GetMapping(params = {"name"})
    public List<CountryDTO> getCountriesByName(@RequestParam String name) {
        return countryService.getCountriesByName(name);
    }

    @GetMapping(params = {"code"})
    public List<CountryDTO> getCountriesByCode(@RequestParam String code) {
        return countryService.getCountriesByCode(code);
    }

    @GetMapping("/{continent}/continent")
    public List<CountryDTO> getCountriesByContinent(@PathVariable String continent) {
        return countryService.getCountriesByContinent(continent);
    }

    @GetMapping("/{language}/language")
    public List<CountryDTO> getCountriesByLanguage(@PathVariable String language) {
        return countryService.getCountriesByLanguage(language);
    }

    @GetMapping("/most-borders")
    public CountryDTO getCountryWithMostBorders() {
        Country country = countryService.getCountryWithMostBorders();
        return country != null ? new CountryDTO(country.getCode(), country.getName()) : null;
    }

    @PostMapping
    public List<CountryDTO> saveCountries(@RequestBody CountryRequestDTO request) {
        return countryService.saveRandomCountries(request.getAmountOfCountryToSave());
    }
}