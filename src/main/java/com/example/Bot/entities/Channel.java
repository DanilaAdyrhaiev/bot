package com.example.Bot.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "channel_name", length = 255)
    private String channelName;

    @Column(name = "link_on_screenshot_1", length = 255)
    private String linkOnScreenshot1;

    @Column(name = "link_on_screenshot_2", length = 255)
    private String linkOnScreenshot2;

    @Column(name = "link_on_admin", length = 255)
    private String linkOnAdmin;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "directoryLink", length = 255)
    private String directoryLink;

    @Column(name = "rate", columnDefinition = "int default 0")
    private int rate;

    public Channel(User user){
        this.user = user;
    }
}
