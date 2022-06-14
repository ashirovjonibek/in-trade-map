package uz.in_trade_map.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.in_trade_map.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {

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

    private boolean accountNonBlocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialNonExpired = true;
    private boolean enabled = true;

    public User(String firstName, String lastName, String phoneNumber, String username, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.roles = roles;
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
