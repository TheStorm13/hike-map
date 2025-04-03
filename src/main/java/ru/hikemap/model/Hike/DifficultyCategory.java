package ru.hikemap.model.Hike;

import jakarta.persistence.*;

@Entity
@Table(name = "difficulty_category")
public class DifficultyCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;
}
