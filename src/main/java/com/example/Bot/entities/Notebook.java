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
    @ManyToOne
    @JoinColumn(name = "channels_id", nullable = false)
    private Channel channel;

    @Column(name = "paymentCode")
    private String paymentCode;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;
}
