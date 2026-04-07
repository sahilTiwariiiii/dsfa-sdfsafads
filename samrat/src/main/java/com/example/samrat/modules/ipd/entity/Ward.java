package com.example.samrat.modules.ipd.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wards")
@Getter
@Setter
public class Ward extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WardType type; // ICU, GENERAL, SEMI_PRIVATE, PRIVATE, EMERGENCY

    private int capacity;
    private boolean active = true;

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bed> beds = new HashSet<>();

    public enum WardType {
        ICU, GENERAL, SEMI_PRIVATE, PRIVATE, EMERGENCY, DELUXE, LABOUR_ROOM, OT
    }
}
