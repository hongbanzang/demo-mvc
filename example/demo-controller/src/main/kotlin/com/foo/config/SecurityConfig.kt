package com.foo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers("/login/**", "/signup", "/resources/**").permitAll()
                .anyRequest().authenticated()
        }
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .logout { logout -> logout.logoutUrl("/signout").deleteCookies("JSESSIONID") }
            .exceptionHandling { t -> t.accessDeniedPage("/resources/error.html") }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val encoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val manager = InMemoryUserDetailsManager()
        val userDetails: UserDetails = User.withUsername("scott")
            .passwordEncoder { p: String -> encoder.encode(p) }
            .password("5555")
            .roles("USER")
            .build()
        manager.createUser(userDetails)
        return manager
    }
}
