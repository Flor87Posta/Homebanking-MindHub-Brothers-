package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test; //JUnit  a través de dependencias de Maven?
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static com.mindhub.homebanking.models.CardType.CREDIT;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
//Las anotaciones @DataJpaTest y @AutoConfigureTestDatabase(replace = NONE)
// indican a Spring que debe escanear en busca de clases @Entity y configurar los repositorios JPA.
// Además hace que las operaciones realizadas en la base de datos sean por defecto transaccionales
// para que luego de ejecutarlas sean revertidas y no afecten los datos reales fuera de las pruebas,
// así como también indicar que se quiere conectar a una base de datos real y no a una embebida en la aplicación H2.
@SpringBootTest // @DataJpaTest es PARA Postgree
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest { //test de integración para comprobar la comunicación entre la aplicación y la base de datos


        @Autowired
        LoanRepository loanRepository;

        @Autowired
        CardRepository cardRepository;

        @Autowired
        AccountRepository accountRepository;


        @Test // aca para pruebas de integración: verifica que existan préstamos en la base de datos, primero busca
        // todos los préstamos y luego verificar que la lista no esté vacía.
        public void existLoans(){
                List<Loan> loans = loanRepository.findAll();
                assertThat(loans,is(not(empty())));
    }


        @Test //aca para pruebas de integración: verifica que en la lista de los préstamos exista uno llamado “Personal”.
            public void existPersonalLoan(){
                List<Loan> loans = loanRepository.findAll();
                assertThat(loans, hasItem(hasProperty("name", is("PERSONAL"))));
            }

    //hasta acá se ejecutan 3 test, uno que ya viene con el esqueleto del proyecto en la clase homebankingAplicationTest
    // y los otros 2 de existLoans y existPersonalLoan


    //assertThat es uno de los métodos JUnit del objeto Assert que se puede utilizar para comprobar si un valor
    // específico coincide con uno esperado.
    //Acepta principalmente 2 parámetros: El primero es el valor real y el segundo es un objeto de coincidencia.
    // Luego intentará comparar estos dos y devuelve un resultado booleano si es una coincidencia o no.
    //En muchos casos se prefiere usar el enfoque que aporta hamCrest con sus machers ya que permite una lectura
    // mas natural del código a través del uso de import estáticos. (que son las librerias importadas hamcrest)


    //test propios de integracion:


        @Test
        public void existCreditCards(){
            List<Card> cards = cardRepository.findAll();
            assertThat(cards, hasItem(hasProperty("typeCard", is(CREDIT))));

        }

        @Test
        public void typeOfAccount(){
            List<Account> accounts = accountRepository.findAll();
            assertThat(accounts, hasItem(hasProperty("number", isA(String.class))));
        }


    //Ahora se crearán  tests unitarios, la diferencia entre ambos es que los de integración buscas ver
    // si la aplicación funciona correctamente con otros componentes, por ej con los test a los repositorios
    // se realizaron verificaciones automáticas para saber si la comunicación es correcta entre la base
    // de datos y la aplicación así como para determinar que el gestor de base de datos estaba retornando
    // los resultados correctos, con los test unitarios se busca probar el funcionamiento lógico de un trozo
    // de código de la aplicación, en estos test no puede entrar en juego la base de datos ya que estaríamos
    // haciendo las dos cosas, solo queremos probar un determinado proceso.
    //Como toda la aplicación en esencia es una API REST para obtener información de la base de datos,
    // no tenemos un caso de un proceso que se deba probar de manera aislada, por eso vas a refactorizar
    // un poco el código de manera que abstraigas un proceso que luego podamos probar.
    //En la API personalizada creaste un servicio para crear las tarjetas de los clientes, /clients/current/cards
    // este servicio de alguna manera debe generar el número de tarjeta y el cvv, estos dos procesos serán extraídos
    // a métodos (refactorizar) para que queden aislados y se puedan realizar pruebas unitarias con ellos.
    //Primero crearás un nuevo paquete llamado utils, allí crearás una nueva clase llamada CardUtils
}



