package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.NamedQuery;
import java.util.List;

//@NamedQuery(name="Client.findByEmail", query="select u from Client u where u.email= ?1")

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long>{

    Client findByEmail (String email);

}

