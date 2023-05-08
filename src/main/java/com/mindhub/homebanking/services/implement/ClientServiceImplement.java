package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

// implementamos una interfaz (ClientService) a nuestra clase CLientServiceImplement,
//nos pide que nuestra clase se comporte de la misma forma que se está comportando la interfaz, por eso tenemos que poner
// implements methods, y ahi nos da el cuerpo de cada método para poder definir que va a hacer cada uno
@Service //le indica a Spring que esta clase /componente va a ser un servicio de nuestra aplicación
public class ClientServiceImplement implements ClientService {

    //como vamos a usar el ClientRepository lo inyectamos tb aca:

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public List<ClientDTO> getClients() { //retorna una lista de ClientDTO, con todos los ClientDTO
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @Override
    public ClientDTO getClient(Long id) { //para retornar un ClientDTO
               return new ClientDTO(this.findById(id)); //usamos el método ya creado abajo de findById, con el this
        //para indicar que es el método que viene después del this pertenece a esta clase
    }

    @Override
    public Client findByEmail(String email) { //retorna un Client por email
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client findById(Long id) { //retorna un Client por id
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void saveNewClient(Client client) { //guarda un nuevo Client en el repo
        clientRepository.save(client);

    }
}
