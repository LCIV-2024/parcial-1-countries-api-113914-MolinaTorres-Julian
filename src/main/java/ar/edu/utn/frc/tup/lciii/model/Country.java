package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long population;
    private double area;
    private String code;
    private String region;

    @ElementCollection
    private List<String> borders;

    @ElementCollection
    @CollectionTable(name = "country_languages", joinColumns = @JoinColumn(name = "country_id"))
    @MapKeyColumn(name = "language")
    private Map<String, String> languages;
}