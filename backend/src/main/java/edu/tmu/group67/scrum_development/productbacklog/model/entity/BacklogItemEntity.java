package edu.tmu.group67.scrum_development.productbacklog.model.entity;

// pkgs
import jakarta.persistence.*;
//import jakarta.websocket.Decoder.Text;
import java.time.LocalDateTime;

import edu.tmu.group67.scrum_development.auth.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import edu.tmu.group67.scrum_development.enums.Status;
import edu.tmu.group67.scrum_development.enums.Level;


//means it represents a table in the database
@Entity
//table name in the database
@Table(name = "backlog_items")
//lombok annotation to generate getters, setters, 
// toString, equals, and hashCode methods
@Data

//gen a no-arg constructor
@NoArgsConstructor

//gens a constructor with all fields being parameters
// i.e. manual creation, like a backlog itm
@AllArgsConstructor
@Builder

// db naming covention is diff then regualr for java/ db is all lowercase + _ between words.
public class BacklogItemEntity {
    @Id //  marks primary key
    // auto gen a id for the key + auto increments
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //backlog title
    // for column, must have some data, and cannot have a identical entry
    @Column(nullable = false, unique = true)
        private String title;

    // backlog req + story
    // var char is only 255, too small
    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
        private String requirements;
        private String story;
    
    // backlog effort
    @Column(nullable = false)
        private int effort;

    // backlog created @
    @Column(name = "created_at", nullable = false) // bc db name is diff from one we use in fxn
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // backlog created BY (big int, from user) - make relationshop
    // https://www.geeksforgeeks.org/advance-java/jpa-many-to-one-mapping/
    // https://kavyajayan.medium.com/understanding-join-fetch-lazy-fetching-and-eager-fetching-in-spring-boot-ce64661f98bd
    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn (name="created_by")     // use @joincolumn for relationship
    private User createdBy;

    // backlog UPdated @ 
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Level priority;

    @Enumerated(EnumType.STRING)
    private Level risk;

    

    // backlog prior (enum)
    // backlog risk (enum)
    // backlog status (enum)
    
     /*@Enumerated(EnumType.STRING)
    @Column(name = "role_id")
    private Role role;*/
}
