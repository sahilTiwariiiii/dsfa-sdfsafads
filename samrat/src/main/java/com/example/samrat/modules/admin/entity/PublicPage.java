package com.example.samrat.modules.admin.entity;

import com.example.samrat.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "public_pages")
@Getter
@Setter
public class PublicPage extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String slug; // e.g., about-us, services, contact

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // HTML content

    private boolean published = true;
}
