package com.bp.service.ClientPerson.service;

import com.bp.service.ClientPerson.model.Client;
import com.bp.service.ClientPerson.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClienteById() {
        Client cliente = new Client();
        cliente.setId(1L);
        cliente.setName("Juan");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Client result = clientService.getClientById(1L);
        assertEquals("Juan", result.getName());
    }

    @Test
    public void testSaveCliente() {
        Client client = new Client();
        client.setName("Maria");

        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.saveClient(client);
        assertEquals("Maria", result.getName());
    }

}
