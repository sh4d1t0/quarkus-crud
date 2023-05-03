package org.orquestador.users.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.orquestador.users.entities.Users;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto
}
