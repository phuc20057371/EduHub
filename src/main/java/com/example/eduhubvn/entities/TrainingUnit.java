package com.example.eduhubvn.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "training_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingUnit {
    // system fields
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    private String title;
    private String description;
    private Integer durationSection;
    private Integer orderSection;
    private boolean isLead;

}
