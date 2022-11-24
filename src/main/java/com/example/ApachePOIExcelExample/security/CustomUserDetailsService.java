package com.example.ApachePOIExcelExample.security;

import com.example.ApachePOIExcelExample.entity.UserEntity;
import com.example.ApachePOIExcelExample.enums.Provider;
import com.example.ApachePOIExcelExample.enums.UserRole;
import com.example.ApachePOIExcelExample.repository.RoleRepository;
import com.example.ApachePOIExcelExample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with the given email");
        }
        return new CustomUserDetails(user);
    }

    public void processOAuthPostLogin(CustomOAuth2User oauthUser) {
        var email = oauthUser.getEmail();
        var name = oauthUser.getName();
        UserEntity existUser = userRepository.findByEmail(email);
        if (existUser == null) {
            var newUser = UserEntity.builder()
                    .email(email)
                    .password("")
                    .fullName(name)
                    .notExpired(true)
                    .notLocked(true)
                    .provider(Provider.GOOGLE)
                    .credentialsNotExpired(true)
                    .isEnabled(true)
                    .lastLogin(Instant.now())
                    .registered(Instant.now())
                    .roles(Set.of(roleRepository.findByUserRole(UserRole.ROLE_USER)))
                    .build();
            userRepository.save(newUser);
            System.out.println("Created new user with email: " + email);
        }
    }

}