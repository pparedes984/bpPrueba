package com.bp.service.ClientPerson.service;

import com.bp.service.ClientPerson.model.Client;
import com.bp.service.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bp.service.ClientPerson.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {
    private static final Logger logger = LogManager.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;


    public List<Client> getAllClients() {
        logger.info("Obteniendo todos los clientes");
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        logger.info("Obteniendo cliente con ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado");
                });
    }

    public Boolean verifyClientExists(Long id) {
        logger.info("Verificando si existe cliente con ID: {}", id);
        return clientRepository.existsById(id);

    }

    public Client saveClient(Client client) {
        logger.info("Creando cliente: {}", client.getName());
        Client savedClient = clientRepository.save(client);
        return clientRepository.save(savedClient);
    }

    public Client updateClient(Long id, Client clientDetails) {
        logger.info("Obteniendo cliente con ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado");
                });
        if (clientDetails.getName() != null)
            client.setName(clientDetails.getName());

        if (clientDetails.getGender() != null)
            client.setGender(clientDetails.getGender());

        if (clientDetails.getAge() > 0)
            client.setAge(clientDetails.getAge());

        if (clientDetails.getDni() != null)
            client.setDni(clientDetails.getDni());

        if (clientDetails.getAddress() != null)
            client.setAddress(clientDetails.getAddress());

        if (clientDetails.getTelephone() != null)
            client.setTelephone(clientDetails.getTelephone());

        if (clientDetails.getPassword() != null)
            client.setPassword(clientDetails.getPassword());

        if (clientDetails.getState() != null)
            client.setState(clientDetails.getState());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        logger.info("Deleting client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cliente con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado");
                });

        clientRepository.delete(client);
    }
}
