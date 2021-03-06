package com.example.demo.config;

import com.example.demo.auth.filter.TokenAuthenticationFilter;
import com.example.demo.auth.service.TokenAuthenticationProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenAuthenticationProvider provider;

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(getPublicUrls());
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    private static List<RequestMatcher> getPublicUrls() {
        List<RequestMatcher> urls = new ArrayList<>();
//        urls.add(new AntPathRequestMatcher("/auth/**"));
//        urls.add(new AntPathRequestMatcher("/demo/**"));
//        urls.add(new AntPathRequestMatcher("/h2/**"));
//        urls.add(new AntPathRequestMatcher("/favicon.ico"));
        urls.add(new NegatedRequestMatcher(new AntPathRequestMatcher("/user/**")));

        return urls;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                // ????????????????????? HttpSession
                .sessionCreationPolicy(STATELESS)
                .and()
                // ????????????????????? Provider
                .authenticationProvider(provider)
                // ??????????????? Filter??????????????? Request Header ??? Token
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                // ??????????????? URL ????????????
                .authorizeRequests()
                .antMatchers("/user").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                // ???????????? REST API ??????????????????
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .headers()
                .frameOptions().disable();
    }

    @Bean
    public TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        // ????????????????????????
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    /**
     * Disable Spring boot automatic filter registration.
     */
    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
        final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userService;
    }

    // ???????????????
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    class NoRedirectStrategy implements RedirectStrategy {

        @Override
        public void sendRedirect(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
            // No redirect is required with pure REST
        }
    }
}
