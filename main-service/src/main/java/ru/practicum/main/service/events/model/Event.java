package ru.practicum.main.service.events.model;

import lombok.*;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.location.model.Location;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.utils.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Comparable<Event> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Category category;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @Builder.Default
    @Column(name = "created_on")
    private LocalDateTime createdOn = now();
    @Column(name = "description")
    private String description;
    @Builder.Default
    @Column(name = "event_date")
    private LocalDateTime eventDate = now().plusHours(2);
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User initiator;
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Location location;
    @Column(name = "paid")
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Builder.Default
    @Column(name = "published_on")
    private LocalDateTime publishedOn = now();
    @Builder.Default
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "title")
    private String title;
    @Transient
    private long views;

    @Override
    public int compareTo(Event e) {
        return Long.compare(this.views, e.views);
    }


}
