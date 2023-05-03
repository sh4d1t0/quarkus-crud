package org.orquestador.users.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.orquestador.users.entities.Users;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto

    public Users findByEmailAndPassword(String email, String password) {
        return find("email = ?1 and password = ?2", email, password).firstResult();
    }
}
