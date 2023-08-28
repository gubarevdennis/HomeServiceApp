package den.gubarev.springproject.security;

import den.gubarev.springproject.models.Resident;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Класс обертка для den.gubarev.springproject.security
public class ResidentDetails implements UserDetails {

    private final Resident resident;

    public ResidentDetails(Resident resident) {
        this.resident = resident;
    }


    // Будем возвращать коллекцию прав для пользователя
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // если нужно настроить действия а не роли - нужно вернуть список из действий
         return Collections.singletonList(new SimpleGrantedAuthority(resident.getRole()));
    }

    @Override
    public String getPassword() {
        return this.resident.getPassword();
    }

    @Override
    public String getUsername() {
        return this.resident.getName();
    }

    // Не прсрочен
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Не просрочен
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Включен
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Нужно чтобы получаить данные аутентифицированного пользователя
    public Resident getResident() {
        return this.resident;
    }
}
