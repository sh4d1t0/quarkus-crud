package org.orquestador.credit.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "credit")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Credit extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT AUTO_INCREMENT")
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "Client Name", example = "John")
    @NotBlank(message = "Client name cannot be null")
    private String clientName;

    @Schema(description = "Client Lastname", example = "Doe")
    @NotBlank(message = "Client lastname cannot be null")
    private String clientLastname;

    @Schema(description = "Client Maternal Lastname", example = "Doe")
    @NotBlank(message = "Client maternal lastname cannot be null")
    private String clientMaternalLastname;

    @Schema(description = "Business relation", example = "Example Relation")
    @NotBlank(message = "Business relation cannot be null")
    private String businessRelation;

    @Schema(description = "Business Name", example = "Example")
    @NotBlank(message = "Business name cannot be null")
    private String businessName;

    @Schema(description = "RFC", example = "John")
    @NotBlank(message = "The RFC cannot be null")
    private String rfc;

    @Schema(description = "Constitution Date", example = "29-08-2023")
    @NotBlank(message = "Constitution date cannot be null")
    private String constitucionDate;

    @Schema(description = "Business Group", example = "Example Group")
    @NotBlank(message = "Business group cannot be null")
    private String businessGroup;

    @Schema(description = "Street", example = "St Example")
    @NotBlank(message = "Street cannot be null")
    private String street;

    @Schema(description = "Outdoor Number", example = "652")
    @NotBlank(message = "Outdoor number cannot be null")
    private String outdoorNumber;

    @Schema(description = "Internal Number", example = "25A")
    @NotBlank(message = "Internal number cannot be null")
    private String internalNumber;

    @Schema(description = "Postal Code", example = "698365")
    @NotBlank(message = "Postal code cannot be null")
    private String postalCode;

    @Schema(description = "Suburb", example = "Example suburb")
    @NotBlank(message = "Suburb cannot be null")
    private String suburb;

    @Schema(description = "Town", example = "Example Town")
    @NotBlank(message = "Town cannot be null")
    private String town;

    @Schema(description = "State", example = "Example State")
    @NotBlank(message = "State cannot be null")
    private String state;

    @Schema(description = "Economic Sector", example = "Example Sector")
    @NotBlank(message = "Economic Sector cannot be null")
    private String economicSector;

    @Schema(description = "Economic Subsector", example = "Example Subsector")
    @NotBlank(message = "Economic subsector cannot be null")
    private String economicSubsector;

    @Schema(description = "Activity Economic", example = "Example Activity")
    @NotBlank(message = "Activity economic cannot be null")
    private String activityEconomic;

    // Constructor, getters y setters
}
