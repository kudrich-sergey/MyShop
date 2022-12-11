package com.example.myshop.config;

import com.example.myshop.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                // Указываем что все страницы должны быть защищены аутентификации
                .authorizeRequests()
                // Указываем что /admin доступин пользователю с ролью администратора
//                .antMatchers("/admin").hasAnyRole("ADMIN")
//                .antMatchers("/user").hasAnyRole("USER")
                // Указываем что не аутентифицированные пользователи могут заходить на страницу с формой аутентификации и на объект ошибки
                // С помощью permitAll указывакем что данные страницы по умолчанию доступны всем пользователям
                .antMatchers("/auth/login", "/error", "/auth/registration", "/", "/product/info/{id}", "/img/**", "/search", "/products/**").permitAll()
                // Указываем что все остальные страницы доступны пользователю с ролью user и admin
//                .antMatchers("/admin/**").hasAnyRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER")
                .anyRequest().hasAnyRole("USER", "ADMIN")
//                // Указываем что для всех остальных страниц необходимо вызывать метод authenticated, который открываем форму аутентификации
//                .anyRequest().authenticated()
                // Указываем что дальше конфигурироваться будет аутентификация и соединяем аутентификацию с настройкой доступа
                .and()
                .formLogin().loginPage("/auth/login")
                // Указываем на какой url адрес будут отправляться данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы. Мы задали url по умолчанию, который позволяет обрабатывать форму аутентификации в спринг секьюрити. Спринг секьюрити будет ждать логин и пароль с формы и затем сверить их с данными в БД
                .loginProcessingUrl("/process_login")
                // Указываем на какой url необходимо направить пользователя после успешной аутентификации
                // Вторым аргументом ставим true чтобы перенаправление на данную страницу шло в любом случае при успешной аутентификации
                .defaultSuccessUrl("/user/products", true)
                // Указываем куда необходимо перенаправить пользователя при проваленной аутентификации
                // В url будет передан объект. Данный объект мы будем проверять на форме и если он есть будет выводить сообщение "Неправильный логин или пароль"
                .failureUrl("/auth/login")
                .and()
                // Указываем что при переходе на  /logout будет очищена сессия пользовате и перенаправление на /auth/login
                .logout().logoutUrl("/logout").logoutSuccessUrl("/products");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().
                antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**", "/templates/**", "/static/img/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
