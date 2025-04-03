package ru.hikemap.model.Hike;

import jakarta.persistence.*;

@Entity
@Table(name = "hike_type")
public class HikeType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;
}
