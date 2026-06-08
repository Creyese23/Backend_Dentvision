package co.edu.sena.Dentvision_Backend.security;

import co.edu.sena.Dentvision_Backend.entity.User;
import co.edu.sena.Dentvision_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrIdentificacion) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailOrIdentificacion)
                .or(() -> userRepository.findByIdentificacion(emailOrIdentificacion))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: "));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
