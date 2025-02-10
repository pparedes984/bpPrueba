package com.bp.service.ClientPerson.repository;

import com.bp.service.ClientPerson.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

