package com.example.blog.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Value("\${spring.security.user.password}")
    val password: String) {


    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): InMemoryUserDetailsManager {
        return User.withUsername("admin")
            .password(passwordEncoder.encode(password))
            .roles("ADMIN")
            .build()
            .let { InMemoryUserDetailsManager(it) }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
