package com.example.Bot.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name", length = 255)
    private String name;

    @NonNull
    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @Column(name = "messageMenu")
    private int messageMenu;

    @Column(name = "previousPage")
    private String previousPage;

    @Column(name = "selectedChannel")
    private Long selectedChannel;

    @Column(name = "coin")
    private Long coin;

    @NonNull
    @Column(name = "status")
    private String status;

    @Column(name = "usingPage")
    private String usingPage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Channel> channels = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Notebook> notebooks = new HashSet<>();

}
