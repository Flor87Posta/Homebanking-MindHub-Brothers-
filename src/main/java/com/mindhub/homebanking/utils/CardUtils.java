package com.mindhub.homebanking.utils;

public final class CardUtils { //modificador "final": esto para que no se puedan heredar clases de la misma
    //Las clases marcadas como finales no se pueden ampliar/extender, por ej la clase String tb es una final Class
    //si pudiéramos extender la clase String, reemplazar cualquiera de sus métodos y sustituir todas las instancias
    // de String con las instancias de nuestra subclase String específica El resultado de las operaciones sobre objetos
    // String se volvería impredecible.

    private CardUtils() { //se agrega el constructor por defecto al que se coloca el modificador private,
        // de esta manera se asegura que no se pueda instanciar la clase.
    }

        public static int getCardCvv() {
            int cardCvv = (int) (Math.random() * 900) + 100;
            return cardCvv;
        }

        public static String getCardNumberConGuiones() {
            long numberGenerated = (long) (Math.random() * 9000000000000000L) + 1000000000000000L;
            String cardNumber = String.format("%016d", numberGenerated);
            String cardNumberConGuiones = cardNumber.substring(0, 4) + "-" + cardNumber.substring(4, 8) + "-" +
                    cardNumber.substring(8, 12) + "-" + cardNumber.substring(12, 16);
            return cardNumberConGuiones;
        }

        //Como los métodos son estáticos, no se necesita instanciar la clase CardUtils para poder usarlos,
        // simplemente indica en el controlador que se ejecutarán los métodos albergados en la clase CardUtils:
        //String cardNumber = CardUtils.getCardNumber();
        //int cvv = CardUtils.getCVV();


    };

