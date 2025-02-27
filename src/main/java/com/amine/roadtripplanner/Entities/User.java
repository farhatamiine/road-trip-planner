package com.amine.roadtripplanner.Entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection="users")
@Data
@CompoundIndex(name = "email_username_idx", def = "{'email':1,'username':1}")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    private ObjectId userId;
    @NotBlank(message = "Please provide a first name")
    private String firstName;
    @Indexed(unique = true)
    @NotBlank(message = "Please provide a username")
    private String username;
    private Set<Role> roles = new HashSet<>();
    private boolean isEnabled = true;
    private boolean isNonLocked = true;
    private boolean isNonExpired = true;
    private boolean isCredentialsNonExpired = true;
    @NotBlank(message = "Please provide a last name")
    private String lastName;
    @Indexed(unique = true)
    @Email(message = "Please provide a valid email address")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phone;
    private String address;
    private String city;
    @DBRef(lazy = true)
    @Builder.Default
    private final List<Trip> tripList = new ArrayList<>();
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    @Version
    private Long version;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return isNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
