package ru.hikemap.model.Hike;

import jakarta.persistence.*;

@Entity
@Table(name = "area")
public class Area {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;
}
