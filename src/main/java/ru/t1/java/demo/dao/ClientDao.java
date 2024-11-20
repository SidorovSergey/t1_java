package ru.t1.java.demo.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientDao {

    private final ClientRepository clientRepository;

    @NonNull
    public Client findById(@NonNull Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Account not found by id=" + id));
    }
}
