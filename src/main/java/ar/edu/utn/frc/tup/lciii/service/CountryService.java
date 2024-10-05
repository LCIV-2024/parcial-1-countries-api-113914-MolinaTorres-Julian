package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryDTO;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<CountryDTO> getAllCountriesDTO() {
                return getAllCountries().stream()
                        .map(country -> new CountryDTO(country.getCode(), country.getName()))
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByName(String name) {
                return getAllCountries().stream()
                        .filter(country -> country.getName().toLowerCase().contains(name.toLowerCase()))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByCode(String code) {
                return getAllCountries().stream()
                        .filter(country -> country.getCode().toLowerCase().contains(code.toLowerCase()))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByContinent(String continent) {
                return getAllCountries().stream()
                        .filter(country -> country.getRegion().equalsIgnoreCase(continent))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByLanguage(String language) {
                return getAllCountries().stream()
                        .filter(country -> {
                                System.out.println("Country: " + country.getName() + ", Languages: " + country.getLanguages());
                                return country.getLanguages() != null && country.getLanguages().containsKey(language);
                        })
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public Country getCountryWithMostBorders() {
                return getAllCountries().stream()
                        .max(Comparator.comparingInt(country -> country.getBorders() != null ? country.getBorders().size() : 0))
                        .orElse(null);
        }

        public List<CountryDTO> saveRandomCountries(int amount) {
                if (amount <= 0 || amount > 10) {
                        throw new IllegalArgumentException("AMOUNT INVALIDO");
                }

                List<Country> allCountries = getAllCountries();
                Collections.shuffle(allCountries);

                List<Country> countriesToSave = allCountries.stream().limit(amount).collect(Collectors.toList());

                List<Country> savedCountries = countryRepository.saveAll(countriesToSave);

                return savedCountries.stream()
                        .map(country -> new CountryDTO(country.getCode(), country.getName()))
                        .collect(Collectors.toList());
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .code((String) nameData.get("code"))
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }
}