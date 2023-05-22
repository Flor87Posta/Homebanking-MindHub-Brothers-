package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Optional;

//el servicio ahora tiene la responsabilidad de la logica de negocios, y de quitarle funciones al controlador, el controlador
//solo va a escuchar y responder las peticiones,
//con estos servicios es mas eficiente a la hora de realizar los testing para encontrar errores, y tb es posible
//reutilizar estos codigos en otros lados
public interface ClientService { //solo declaro los métodos... despues en los implements los defino(establezco que hace
    //cada método
    List<ClientDTO> getClients();
    ClientDTO getClient(Long id);
    Client findByEmail(String email);

    Client  findById(Long id);
    void saveNewClient(Client client);
}
