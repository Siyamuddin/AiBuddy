package com.example.SpringAI.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "local_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(updatable = true)
    private String password;
    @Column(updatable = true)
    private String university;
    private String department;
    private String country;

    private String provider;
    private String providerId;
    private String image;

    @CreationTimestamp
    private Date creationTime;
    @LastModifiedDate
    private Date lastUpdatedTime;

    @OneToMany(mappedBy = "localUser",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<UserClass> userClasses = new ArrayList<>();

    @OneToMany(mappedBy = "localUser",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Slide> userSlides = new ArrayList<>();

}
