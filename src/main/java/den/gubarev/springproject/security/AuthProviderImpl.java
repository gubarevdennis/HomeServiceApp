//package den.gubarev.springproject.security;
//
//
//import den.gubarev.springproject.services.EmployeeDetailsService;
//import den.gubarev.springproject.services.ResidentDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//@Component
//public class AuthProviderImpl implements AuthenticationProvider {
//
//    private final ResidentDetailsService residentDetailsService;
//    private final EmployeeDetailsService employeeDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public AuthProviderImpl(ResidentDetailsService residentDetailsService, EmployeeDetailsService employeeDetailsService,
//                            PasswordEncoder passwordEncoder) {
//        this.residentDetailsService = residentDetailsService;
//        this.employeeDetailsService = employeeDetailsService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//
//        UserDetails residentDetails = residentDetailsService.loadUserByUsername(username);
//        UserDetails employeeDetails = employeeDetailsService.loadUserByUsername(username);
//
//        String password = passwordEncoder.encode(authentication.getCredentials().toString());
//        System.out.println(authentication.getCredentials().toString());
//        System.out.println(password);
//
//        boolean residentCorrectPassword = false;
//        boolean employeeCorrectPassword = false;
//
//        // Сравниваем имена с базой данных
//        if (!(residentDetails == null)) {
//            residentCorrectPassword = password.equals(residentDetails.getPassword());
//        } else if (!(employeeDetails == null)) {
//            employeeCorrectPassword = password.equals(employeeDetails.getPassword());
//        }
//        else {
//            throw new UsernameNotFoundException("Пользователя с указанным именем не существует!");
//        }
//
//        // Сравниваем пароли
//        if (residentCorrectPassword) {
//            return new UsernamePasswordAuthenticationToken(residentDetails,password,
//                    Collections.emptyList());
//        } else if (employeeCorrectPassword) {
//            return new UsernamePasswordAuthenticationToken(employeeDetails,password,
//                    Collections.emptyList());
//        }
//        else {
//            throw new BadCredentialsException("Некорректный пароль!");
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//
//}
