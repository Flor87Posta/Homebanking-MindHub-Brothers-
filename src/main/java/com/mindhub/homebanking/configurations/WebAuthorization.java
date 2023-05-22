package com.mindhub.homebanking.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().authorizeRequests()

                //                PERMIT ALL
                .antMatchers("/web/index.html/").permitAll()
                .antMatchers("/web/login.html").permitAll()
                .antMatchers("/web/assets/**").permitAll()
                .antMatchers("/posnet.html").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients/current/pay-card").permitAll()


//                ADMIN
                .antMatchers(HttpMethod.POST, "/api/loans/admin-loan").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/cards").hasAuthority("ADMIN")
                .antMatchers("/api/clients").hasAuthority("ADMIN")
                .antMatchers("/api/accounts").hasAuthority("ADMIN")
                .antMatchers( "/api/cards").hasAuthority("ADMIN")
                .antMatchers("/manager.html").hasAuthority("ADMIN")
                .antMatchers("/h2-console").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")



//                CLIENT
                .antMatchers(HttpMethod.POST, "/api/clients/current").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/pay-loan").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/delete-card").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/delete-account").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/export-pdf").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/api/loans").hasAuthority("CLIENT")
                .antMatchers("/api/accounts/**").hasAuthority("CLIENT")
                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/web/account.html").hasAuthority("CLIENT")
                .antMatchers("/web/created-cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/transfers.html").hasAuthority("CLIENT")
                .antMatchers("/web/loan-application.html").hasAuthority("CLIENT");


// para cualquier otro tipo de peticion:
           /*     .anyRequest().denyAll();*/

        http.formLogin() // ruta d acceso al login, método del http security q trabaja el login, defino las reglas del login
        //
                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login"); //solo recibe peticiones POST este metodo


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens: esos tokens hacen que cada cosa que salga del back salga con token, para que cuando se
        // realicen peticiones haga match con cada cookie, pero no genera la cookie o token del session ID (eso se maneja aparte con lo que hicimos arriba)
        // y esos tokens hacen intransferibles los JSESSIONID para que no los puedan copiar
        //si los habilito no funciona como API, porq necesita acceso de otros programas, como h2 etc etc;
        http.csrf().disable();


        //disabling frameOptions so h2-console can be accessed:

        http.headers().frameOptions().disable(); // e-frame (como los q usamos en google maps): son configuraciones preestablecidas o por defecto de h2 console, cada metodo devuelve un objeto http
        // las desactivamos a esas configuraciones (que son las que arman la consola desde cero) para poder usar h2

        // if user is not authenticated, just send an authentication failure response: //cuando el usuario quiere entrar en algun del sitio web me fijo si esta o no autenticado

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication:

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req)); //ejecuta el metodo de abajo para manejar el exito del login

        // if login fails, just send an authentication failure response:

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); //si login falla manda un mns,
        // req: peticion q recibimos del cliente, res: respuesta que vamos a mandar, exc: excepción


        // if logout is successful, just send a success response:

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        //Esta clase se utiliza para manejar el éxito de la operación de logout en una aplicación web protegida por Spring Security.
        // Cuando el usuario inicia sesión, se crea una sesión para él en el servidor, y cuando el usuario cierra sesión,
        // se debe eliminar la sesión.
        //La clase "HttpStatusReturningLogoutSuccessHandler" es responsable de devolver una respuesta HTTP adecuada después
        // de que el usuario haya cerrado sesión correctamente en la aplicación. Por defecto,
        // devuelve un código de estado HTTP 200 (OK), lo que indica que la operación se ha completado con éxito.

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

    }

