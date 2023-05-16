package com.mindhub.homebanking;
import com.mindhub.homebanking.utils.CardUtils;
import org.hamcrest.collection.IsMapWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.aop.support.MethodMatchers.matches;


@SpringBootTest
public class CardUtilsTests {



    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumberConGuiones();
        assertThat(cardNumber,is(not(emptyOrNullString())));

    }



    @Test
    public void cvvIsCreated(){
        int cvv = CardUtils.getCardCvv();
        assertThat(cvv, is(not(nullValue())));

 }

    @Test
    public void cardNumberFormat() {
        String cardNumber = CardUtils.getCardNumberConGuiones();
        assertThat(cardNumber, matchesPattern("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    @Test
    public void cvvDigits() {
        int cvv = CardUtils.getCardCvv();
        assertThat(String.valueOf(cvv), hasToString(matchesPattern("\\d{3}")));
    }
//Primero se convierte el número cvv a una cadena utilizando String.valueOf(cvv), sino
// no me dejaba.
// Luego, con el matcher hasToString(matchesPattern("\\d{3}"))
// se verifica que la cadena del número tenga exactamente 3 dígitos.
// El patrón \\d{3} representa tres dígitos consecutivos.

}
