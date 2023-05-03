package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findById (String Id); //String porque en este caso defini la propiedad con este tipo de dato
    //	/** En el CRUD repository del JPA el metodo esta como optional,
    //	 * Retrieves an entity by its id.
    //	 *
    //	 * @param id must not be {@literal null}.
    //	 * @return the entity with the given id or {@literal Optional#empty()} if none found.
    //	 * @throws IllegalArgumentException if {@literal id} is {@literal null}.
    //	 */
    //	Optional<T> findById(ID id);


}
