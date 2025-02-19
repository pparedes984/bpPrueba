package com.bp.service.client.service;

import com.bp.service.client.model.dto.ClientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    Boolean verifyClientExists(Long id);
    ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClient(Long id, ClientDTO clientDetails);
    void deleteClient(Long id);

}
