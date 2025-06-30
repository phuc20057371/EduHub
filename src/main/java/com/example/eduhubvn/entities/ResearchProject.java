package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "research_project")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchProject {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    @Column(name = "research_area")
    private String researchArea;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "founding_amount")
    private Double foundingAmount;
    @Column(name = "founding_source")
    private String foundingSource;
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;
    @Column(name = "role_in_project")
    private String roleInProject;
    @Column(name = "published_url")
    private String publishedUrl;
    private String status;
    private String description;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
