package org.orquestador.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.orquestador.entities.Users;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto
}
