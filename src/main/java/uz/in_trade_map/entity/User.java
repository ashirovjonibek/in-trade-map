package uz.in_trade_map.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    @CreatedBy
    private User createdBy;

    @LastModifiedBy
    private User updatedBy;

    private boolean active = true;

    private String firstName;

    private String lastName;

    private String middleName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    @ManyToOne
    private Location location;

    @OneToOne
    private Attachment image;

    @ManyToOne
    private Company company;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private boolean accountNonBlocked;
    private boolean accountNonExpired;
    private boolean credentialNonExpired;
    private boolean enabled;

    public User() {
        this.accountNonBlocked = true;
        this.accountNonExpired = true;
        this.credentialNonExpired = true;
        this.enabled = true;
    }

    public User(String firstName, String lastName, String phoneNumber, String username, String password, Set<Role> roles, boolean accountNonBlocked, boolean active, boolean accountNonExpired, boolean credentialNonExpired, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.accountNonBlocked = accountNonBlocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialNonExpired = credentialNonExpired;
        this.enabled = enabled;
        this.active = active;
    }

    public User(boolean active, String username, String password, Company company, Set<Role> roles, Application application) {
        this.active = active;
        this.firstName = application.getFirstName();
        this.lastName = application.getLastName();
        this.middleName = application.getMiddleName();
        this.phoneNumber = application.getBossPhone();
        this.username = username;
        this.password = password;
        this.email = application.getBossEmail();
        this.company = company;
        this.roles = roles;
        this.accountNonBlocked = true;
        this.accountNonExpired = true;
        this.credentialNonExpired = true;
        this.enabled = true;
        this.active = true;
    }

    private Collection<SimpleGrantedAuthority> getPermissions() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        });
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPermissions();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
