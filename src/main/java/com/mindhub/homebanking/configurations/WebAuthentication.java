package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    private ClientRepository clientRepository; // encapsulo porque pasa a ser una propiedad de WebAuthentication

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputEmail-> {

            Client findClient = clientRepository.findByEmail(inputEmail); //paso un client, porque ya estoy recorriendo la lista de clientes del repo

            if (findClient != null) {

                if (findClient.getEmail().contentEquals("florys_211@hotmail.com")) {
                    return new User(findClient.getEmail(), findClient.getPassword(),

                            AuthorityUtils.createAuthorityList("ADMIN"));
                } else {

                return new User(findClient.getEmail(), findClient.getPassword(),

                        AuthorityUtils.createAuthorityList("CLIENT"));
                }} else {

                throw new UsernameNotFoundException("Unknown user: " + inputEmail); // igual al manejo del error lo hacemos con las respuestas de codigos de estados 401 etc

            }

        });


    }




}


