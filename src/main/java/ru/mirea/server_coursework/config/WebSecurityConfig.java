package ru.mirea.server_coursework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.mirea.server_coursework.model.Permission;
import ru.mirea.server_coursework.security.JwtAuthenticationEntryPoint;
import ru.mirea.server_coursework.security.JwtTokenFilter;
import ru.mirea.server_coursework.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(JwtTokenFilter filter, JwtAuthenticationEntryPoint entryPoint,
                             PasswordEncoder passwordEncoder, UserService userService) {
        this.jwtTokenFilter = filter;
        this.jwtAuthenticationEntryPoint = entryPoint;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and().csrf().disable()
            .authorizeRequests() //Запрос на вход
            .antMatchers("/auth/**", "/posts", "/posts/filter", "/categories").permitAll()
            .antMatchers("/admin/**").hasAuthority(Permission.ADMIN_PERMISSION.getPermission())
            .anyRequest().authenticated()
            .and().exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        var configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return configurationSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.security.cors")
    public CorsConfiguration corsConfiguration() {
        return new CorsConfiguration();
    }
}
