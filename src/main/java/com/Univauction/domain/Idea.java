package com.Univauction.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ideas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ideaId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String category;

    private String tags;

    @Column(columnDefinition = "TEXT")
    private String problemStatement;

    @Column(columnDefinition = "TEXT")
    private String solutionOverview;

    @Column(columnDefinition = "TEXT")
    private String marketOpportunity;

    @Column(columnDefinition = "TEXT")
    private String competitiveAdvantage;

    @Column(columnDefinition = "TEXT")
    private String filesUrl;
}
