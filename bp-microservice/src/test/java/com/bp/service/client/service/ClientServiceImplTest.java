package com.bp.service.client.service;

import com.bp.service.client.model.Client;
import com.bp.service.client.model.dto.ClientDTO;
import com.bp.service.client.model.ClientState;
import com.bp.service.client.model.PersonGender;
import com.bp.service.client.repository.ClientRepository;
import com.bp.service.client.service.impl.ClientServiceImpl;
import com.bp.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setGender(PersonGender.MASCULINO);
        client.setAge(30);
        client.setDni("12345678");
        client.setAddress("123 Main St");
        client.setTelephone("1234567890");
        client.setPassword("securepassword");
        client.setState(ClientState.ACTIVO);

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("John Doe");
        clientDTO.setGender("MASCULINO");
        clientDTO.setAge(30);
        clientDTO.setDni("12345678");
        clientDTO.setAddress("123 Main St");
        clientDTO.setTelephone("123-456-7890");
        clientDTO.setPassword("securepassword");
        clientDTO.setState("ACTIVO");
    }

    @Test
    void testGetAllClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientDTO> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetClientById_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(1L);
        });

        assertEquals("Cliente no encontrado", thrown.getMessage());
    }

    @Test
    void testSaveClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO result = clientService.saveClient(clientDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testUpdateClient_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO updatedDTO = new ClientDTO();
        updatedDTO.setName("John Updated");
        updatedDTO.setGender("MASCULINO");
        updatedDTO.setAge(31);
        updatedDTO.setDni("12345678");
        updatedDTO.setAddress("Updated Street");
        updatedDTO.setTelephone("9876543210");
        updatedDTO.setState("ACTIVO");
        updatedDTO.setPassword("newpassword");
        ClientDTO result = clientService.updateClient(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals(31, result.getAge());
    }

    @Test
    void testUpdateClient_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.updateClient(1L, clientDTO);
        });

        assertEquals("Cliente no encontrado", thrown.getMessage());
    }

    @Test
    void testDeleteClient_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void testDeleteClient_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.deleteClient(1L);
        });

        assertEquals("Cliente no encontrado", thrown.getMessage());
    }
}

