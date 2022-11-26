package com.example.ApachePOIExcelExample.config;

import com.example.ApachePOIExcelExample.security.CustomOAuth2User;
import com.example.ApachePOIExcelExample.security.CustomOAuth2UserService;
import com.example.ApachePOIExcelExample.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //declares which Page(URL) will have What access type
        http.authorizeRequests()
                .antMatchers("/", "/login", "/oauth/**", "/array_column_example/**", "/users").permitAll()
//                .antMatchers("/welcome", "/common", "/get_report").authenticated()
                .antMatchers("/user").hasAuthority("ROLE_USER")
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .antMatchers("/owner").hasAuthority("ROLE_OWNER")

                // Any other URLs which are not configured in above antMatchers
                // generally declared aunthenticated() in real time
                .anyRequest().authenticated()

                // Login Form Details
                .and()
                .formLogin().permitAll()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("pass")
                    .defaultSuccessUrl("/welcome", true)
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oauthUserService)
                    .and()
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                            Authentication authentication) throws IOException, ServletException {
                            System.out.println("AuthenticationSuccessHandler invoked");
                            System.out.println("Authentication name: " + authentication.getName());
                            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                            customUserDetailsService.processOAuthPostLogin(oauthUser);
                            response.sendRedirect("/welcome");
                        }
                    })
                    //.defaultSuccessUrl("/welcome")

                // Logout Form Details
                .and()
                .logout().logoutSuccessUrl("/").permitAll()

                .and().cors().and().csrf().disable();

        return http.build();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode("1234");
        System.out.println(pwd);
    }

}