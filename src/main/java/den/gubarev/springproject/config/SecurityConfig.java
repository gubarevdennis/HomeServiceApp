package den.gubarev.springproject.config;

import den.gubarev.springproject.services.EmployeeDetailsService;
import den.gubarev.springproject.services.ResidentAndEmployeeDetailsService;
import den.gubarev.springproject.services.ResidentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ResidentDetailsService residentDetailsService;
    private final EmployeeDetailsService employeeDetailsService;
    private final ResidentAndEmployeeDetailsService residentAndEmployeeDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(ResidentDetailsService residentDetailsService, EmployeeDetailsService employeeDetailsService,
                          ResidentAndEmployeeDetailsService residentAndEmployeeDetailsService, JWTFilter jwtFilter) {
        this.residentDetailsService = residentDetailsService;
        this.employeeDetailsService = employeeDetailsService;
        this.residentAndEmployeeDetailsService = residentAndEmployeeDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // конфигурируем сам Spring Security
        // конфигурируем авторизацию
        http
                .csrf().ignoringAntMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/auth/admin").hasRole("ADMIN")
                .antMatchers("/auth/login","/auth/registration-employee","/api/**",
                        "/auth/registration-resident", "/error").permitAll()
                .anyRequest().hasAnyRole("RESIDENT","EMPLOYEE","ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/resident",true )
                .failureForwardUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

// Настраивает конфигурацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(residentAndEmployeeDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Мой собственный менеджер для аутентификации посредством токена
    @Bean
    public AuthenticationManager authenticationManagerJWT() throws Exception {
        return super.authenticationManagerBean();
    }

}
