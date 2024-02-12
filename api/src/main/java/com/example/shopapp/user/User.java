package com.example.shopapp.user;

import com.example.shopapp.address.Address;
import com.example.shopapp.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "_user")
//@Getter
//@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long userId;
    @Column(
            nullable = false,
            length = 50
    )
    private String firstName;
    @Column(
            nullable = false,
            length = 50
    )
    private String lastName;
    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String email;
    @Column(
            nullable = false,
            length = 100
    )
    private String password;
    @Column(
            nullable = false
    )
    private LocalDateTime birthDate;
    @Column(
            nullable = false,
            length = 7
    )
    @Enumerated(
            EnumType.STRING
    )
    private Gender gender;
    @Column(
            unique = true,
            length = 10
    )
    private String phoneNumber;
    @Column(
            nullable = false,
            length = 9
    )
    @Enumerated(
            EnumType.STRING
    )
    private Role role;
    @OneToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            nullable = false,
            name = "address_id",
            referencedColumnName = "addressId"
    )
    private Address address;
    @OneToMany(
            mappedBy = "user"
    )
    private Set<Order> orders;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
