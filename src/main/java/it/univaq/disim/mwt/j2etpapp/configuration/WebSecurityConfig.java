package it.univaq.disim.mwt.j2etpapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
// enabling PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: all missing routes and control if correct
        http.headers().disable().csrf().disable().formLogin()
                // login
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .and()
                // remember me
                .rememberMe()
                // remember me: checkbox parameter remember
                .rememberMeParameter("remember")
                .key("j2etpapp-remember")
                // remember me: 1 day
                .tokenValiditySeconds(86400)
                .and()
                // logout
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll().and()
                .authorizeRequests()
                // url protection (from strictest to most permissive)
                .antMatchers("/admin").hasRole("administrator")
                .antMatchers("/admin/**").hasRole("administrator")
                .antMatchers("/home/**").hasAnyRole("administrator", "logged")
                .antMatchers("/home").hasAnyRole("administrator", "logged")
                .antMatchers("/notifications/**").hasAnyRole("administrator", "logged")
                .antMatchers("/notifications").hasAnyRole("administrator", "logged")
                .antMatchers("/", "/static/**", "/favicon.ico").permitAll();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
