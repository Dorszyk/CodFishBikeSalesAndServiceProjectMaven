package com.codfish.bikeSalesAndService.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
@Getter
@Setter
@EqualsAndHashCode(of = "personRepairingId")
@ToString(of = {"personRepairingId", "name", "surname", "codeNameSurname","userId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person_repairing")
public class PersonRepairingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_repairing_id")
    private Integer personRepairingId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "code_name_surname")
    private String codeNameSurname;

    @Column(name = "userId")
    private Integer userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personRepairing")
    private Set<ServicePersonEntity> servicePerson;
}
