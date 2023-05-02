package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {


    Account findByNumber(String number);

    //dentro del JPA Repository esta la interfaz QueryByExampleExecutor, y dentro de la misma hay un método boolean exists(Example<S> example);
    // que es el que me sirve para Verificar que exista la cuenta de origen / destino que voy a usar en las transferencias (TransactionController)

    boolean existsByNumber(String number);
}
