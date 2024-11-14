package ru.t1.java.demo.dao;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TxDao {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T requiresNew(@NonNull Supplier<T> supplier) {
        return supplier.get();
    }

}
