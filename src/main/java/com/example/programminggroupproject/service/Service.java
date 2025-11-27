package com.example.programminggroupproject.service;

import java.util.ArrayList;
import java.util.UUID;

public interface Service<T> {
    T get(UUID id);
    ArrayList<T> getAll();
    T put(UUID id, T object);
    T post(T object);
    void delete(UUID id);
    void deleteAll();
}
