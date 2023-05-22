package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    Account findByNumber(String number);


    boolean existsByNumber(String number); //dentro del JPA Repository esta la interfaz QueryByExampleExecutor,
    // y dentro de la misma hay un método boolean exists(Example<S> example);
    // que es el que me sirve para Verificar que exista la cuenta de origen / destino que voy a usar en las
    // transferencias (TransactionController)

    @Query ("SELECT a FROM Account a WHERE a.client = :client AND a.hidden = false" )
    List<Account> findByClient(@Param("client") Client client);
   // método que arroja la lista de cuentas del cliente y que no están ocultas, es decir que estan activas o en uso

//SELECT a: Indica que la consulta seleccionará entidades de tipo Account y las referenciará como a. Es decir,
// a será un alias para la entidad Account en la consulta.
//FROM Account a: Especifica la entidad desde la cual se seleccionarán los datos. En este caso, se seleccionan
// entidades de tipo Account y se les asigna el alias a.
//WHERE a.client = :client: Establece una condición para filtrar los resultados. La condición indica que
// la propiedad client de la entidad Account debe ser igual al parámetro client proporcionado. Aquí se espera
// que client sea un objeto de tipo Client.
//AND a.isHidden = false: Agrega una segunda condición para filtrar los resultados. Esta condición indica que
// la propiedad isHidden de la entidad Account debe ser false. Solo se seleccionarán las cuentas que no estén ocultas.
//En resumen, la consulta seleccionará todas las entidades Account que cumplan dos condiciones: tener un cliente
// igual al parámetro client y no estar ocultas (propiedad isHidden igual a false). Asegúrate de que el tipo y
// nombre del parámetro client sean correctos y coincidan con la definición de la clase Client.

}
