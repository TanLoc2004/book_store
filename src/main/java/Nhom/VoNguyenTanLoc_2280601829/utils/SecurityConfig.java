package Nhom.VoNguyenTanLoc_2280601829.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import
org.springframework.security.core.userdetails.UserDetailsService;
import
org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import
org.springframework.security.crypto.password.PasswordEncoder;
import
org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import
org.springframework.security.web.SecurityFilterChain;

import
Nhom.VoNguyenTanLoc_2280601829.services.OAuthService;
import Nhom.VoNguyenTanLoc_2280601829.services.UserService;
import
jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
private final OAuthService oAuthService;
private final UserService userService;
@Bean
public UserDetailsService userDetailsService() {
return new UserService();
}
@Bean
public PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    // ctor takes the UserDetailsService in this version of Spring Security
    var auth = new DaoAuthenticationProvider(userDetailsService());
    auth.setPasswordEncoder(passwordEncoder());
    return auth;
}
@Bean
public SecurityFilterChain securityFilterChain(@NotNull
HttpSecurity http) throws Exception {
return http
.authorizeHttpRequests(auth -> auth
.requestMatchers(
    "/css/**",
    "/js/**",
    "/images/**",
    "/",
    "/oauth/**",
    "/register",
    "/error"
).permitAll()

.requestMatchers("/books/edit/**", "/books/add", "/books/delete")
.authenticated()

.requestMatchers("/books", "/cart", "/cart/**")

.authenticated()

.requestMatchers("/api/**")

.authenticated()
.anyRequest().authenticated()
).logout(logout -> logout
.logoutUrl("/logout")
.logoutSuccessUrl("/login")
.deleteCookies("JSESSIONID")
.invalidateHttpSession(true)
.clearAuthentication(true)
.permitAll()
).formLogin(formLogin -> formLogin
.loginPage("/login")
.loginProcessingUrl("/login")
.defaultSuccessUrl("/")
.failureUrl("/login?error")
.permitAll()
).oauth2Login(
oauth2Login -> oauth2Login.loginPage("/login")
.failureUrl("/login?error")
.userInfoEndpoint(userInfoEndpoint ->
userInfoEndpoint
.userService(oAuthService)
)

.successHandler(

(request, response,
authentication) -> {
var oidcUser =
(DefaultOidcUser) authentication.getPrincipal();
userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getName());
response.sendRedirect("/");
}
)

.permitAll()

).rememberMe(rememberMe -> rememberMe
.key("hutech")
.rememberMeCookieName("hutech")
.tokenValiditySeconds(24 * 60 * 60)
.userDetailsService(userDetailsService())
).exceptionHandling(exceptionHandling ->
exceptionHandling
.accessDeniedPage("/403")
).sessionManagement(sessionManagement ->
sessionManagement
.maximumSessions(1)
.expiredUrl("/login")
).httpBasic(httpBasic -> httpBasic
.realmName("hutech")
).build();
}

}