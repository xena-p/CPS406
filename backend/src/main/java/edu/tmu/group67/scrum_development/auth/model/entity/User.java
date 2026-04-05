package edu.tmu.group67.scrum_development.auth.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


/*NOTES
string defaults to varchar(255) in the database, which is usually sufficient for most use cases. If you need a longer string, you can specify the length using the @Column annotation, like this:
@Column(length = 500)
for text type: @Column(columnDefinition = "TEXT", nullable = false)
*/

@Entity //means it represents a table in the database
@Table(name = "users") //table name in the database
@Data //lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor //lombok annotation to generate a no-argument constructor
@AllArgsConstructor //lombok annotation to generate a constructor with all fields
@Builder

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id")
    private Role role;

    @Column(name = "failed_logins")
    private int failedLogins;

    @Column(name = "account_lock")
    private boolean accountLock;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonLocked() { return !accountLock; }
    @Override public boolean isEnabled() { return true; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

}
