package com.example.Bot.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notebook")
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    @Column(name = "coins_quantity", nullable = false)
    private int coinsQuantity;

    @NonNull
    @Column(name = "success", nullable = false)
    private boolean success;
}
