package com.example.eduhubvn.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "degree")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // e.g., Bachelor's, Master's, PhD
    private String major; // e.g., Computer Science, Mathematics
    private String institution; // e.g., University of XYZ
    @Column(name = "start_year")
    private Integer startYear;
    @Column(name = "graduation_year")
    private Integer graduationYear; // e.g., 2020, 2021
    private String level;
    private String url;
    @Column(name = "reference_id")
    private String referenceID;
    @Column(name = "req_no")
    private String reqNo ;
    private String description;


    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
