package com.bp.service.client.controller;


import com.bp.service.client.model.dto.ClientDTO;
import com.bp.service.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        clientDTO = new ClientDTO();
        clientDTO.setName("John");
        clientDTO.setGender("MASCULINO");
        clientDTO.setAge(31);
        clientDTO.setDni("123456789");
        clientDTO.setAddress("Updated Street");
        clientDTO.setTelephone("9876543210");
        clientDTO.setState("ACTIVO");
        clientDTO.setPassword("newpassword");
    }

    @Test
    void testGetAllClients() {
        // Arrange
        when(clientService.getAllClients()).thenReturn(List.of(clientDTO));

        // Act
        ResponseEntity<List<ClientDTO>> response = clientController.getAllClients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetClientById() {
        // Arrange
        when(clientService.getClientById(1L)).thenReturn(clientDTO);

        // Act
        ResponseEntity<ClientDTO> response = clientController.getClientById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getName());
    }

    @Test
    void testCreateClient() {
        // Arrange
        when(clientService.saveClient(clientDTO)).thenReturn(clientDTO);

        // Act
        ResponseEntity<ClientDTO> response = clientController.createClient(clientDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getName());
    }

    @Test
    void testUpdateClient() {
        // Arrange
        ClientDTO updatedClientDTO = new ClientDTO();
        updatedClientDTO.setName("John Updated");
        updatedClientDTO.setGender("MASCULINO");
        updatedClientDTO.setAge(31);
        updatedClientDTO.setDni("12345678");
        updatedClientDTO.setAddress("Updated Street");
        updatedClientDTO.setTelephone("987654321");
        updatedClientDTO.setState("ACTIVO");
        updatedClientDTO.setPassword("newpassword");
        when(clientService.updateClient(1L, updatedClientDTO)).thenReturn(updatedClientDTO);

        // Act
        ResponseEntity<ClientDTO> response = clientController.updateClient(1L, updatedClientDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("987654321", response.getBody().getTelephone());
    }

    @Test
    void testDeleteClient() {
        // Arrange
        doNothing().when(clientService).deleteClient(1L);

        // Act
        ResponseEntity<Void> response = clientController.deleteClient(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).deleteClient(1L);
    }
}

