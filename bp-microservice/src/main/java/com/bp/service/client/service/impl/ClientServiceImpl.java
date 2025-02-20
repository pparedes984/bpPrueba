package com.bp.service.client.service.impl;

import com.bp.service.client.model.Client;
import com.bp.service.client.model.ClientState;
import com.bp.service.client.model.dto.ClientDTO;
import com.bp.service.client.model.PersonGender;
import com.bp.service.client.service.ClientService;
import com.bp.service.exception.ResourceNotFoundException;
import com.bp.service.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public List<ClientDTO> getAllClients() {
        log.info("Obteniendo todos los clientes");
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientDTO getClientById(Long id) {
        log.info("Obteniendo cliente con ID: {}", id);
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Cliente con ID {} no encontrado", id);
                return new ResourceNotFoundException("Cliente no encontrado");
            });
        return  convertToDTO(client);
    }

    @Transactional
    public Boolean verifyClientExists(Long id) {
        log.info("Verificando si existe cliente con ID: {}", id);
        return clientRepository.existsById(id);

    }

    @Transactional
    public ClientDTO saveClient(ClientDTO clientDTO) {
        log.info("Creando cliente: {}", clientDTO.getName());
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO clientDetails) {
        log.info("Obteniendo cliente con ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado");
                });
        if (clientDetails.getName() != null)
            client.setName(clientDetails.getName());

        if (clientDetails.getGender() != null)
            client.setGender(PersonGender.valueOf(clientDetails.getGender().toUpperCase()));

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
            client.setState(ClientState.valueOf(clientDetails.getState().toUpperCase()));

        Client updatedClient = clientRepository.save(client);
        return convertToDTO(updatedClient);
    }

    @Transactional
    public void deleteClient(Long id) {
        log.info("Deleting client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado");
                });

        clientRepository.delete(client);
    }

    private ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setGender(client.getGender().name()); // Convertir Enum a String
        clientDTO.setAge(client.getAge());
        clientDTO.setDni(client.getDni());
        clientDTO.setAddress(client.getAddress());
        clientDTO.setTelephone(client.getTelephone());
        clientDTO.setState(client.getState().name()); // Convertir Enum a String
        return clientDTO;
    }

    // Convertir de DTO a Entity
    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        client.setGender(PersonGender.valueOf(dto.getGender().toUpperCase())); // Convertir String a Enum
        client.setAge(dto.getAge());
        client.setDni(dto.getDni());
        client.setAddress(dto.getAddress());
        client.setTelephone(dto.getTelephone());
        client.setState(ClientState.valueOf(dto.getState().toUpperCase())); // Convertir String a Enum
        client.setPassword(dto.getPassword());
        return client;
    }
}
