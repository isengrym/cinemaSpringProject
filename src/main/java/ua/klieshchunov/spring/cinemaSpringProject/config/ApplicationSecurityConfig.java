package ua.klieshchunov.spring.cinemaSpringProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import java.util.concurrent.TimeUnit;

import static ua.klieshchunov.spring.cinemaSpringProject.config.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    .authorizeRequests()
                    .antMatchers("/profile/**","ticket").hasAnyRole(USER.name(), ADMIN.name(), MANAGER.name())
                    .antMatchers(HttpMethod.POST,"/admin/users/**","ticket").hasAnyRole(ADMIN.name())
                    .antMatchers(HttpMethod.PUT,"/admin/users/**","ticket").hasAnyRole(ADMIN.name())
                    .antMatchers(HttpMethod.DELETE,"/admin/users/**","ticket").hasAnyRole(ADMIN.name())
                    .antMatchers("/admin/**","ticket").hasAnyRole(ADMIN.name(), MANAGER.name())
                    .antMatchers("/**", "/css/*", "/js/*").permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .passwordParameter("password")
                    .usernameParameter("email")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(30))
//                    .key("g32klsal@")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails userAlexey = User.builder()
                .username("alexey1@gmail.com")
                .password(passwordEncoder.encode("alexey1@gmail.com"))
                .roles(USER.name())
//                .authorities(USER.getGrantedAuthority())
                .build();

        UserDetails userEgor = User.builder()
                .username("egor")
                .password(passwordEncoder.encode("egor"))
                .roles(ADMIN.name())
//                .authorities(ADMIN.getGrantedAuthority())
                .build();

        UserDetails userDmitriy = User.builder()
                .username("dima")
                .password(passwordEncoder.encode("dima"))
                .roles(MANAGER.name())
//                .authorities(MANAGER.getGrantedAuthority())
                .build();

        return new InMemoryUserDetailsManager(
                userAlexey, userEgor, userDmitriy
        );
    }

}
