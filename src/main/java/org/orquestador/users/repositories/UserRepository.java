package org.orquestador.users.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.orquestador.users.entities.Users;
import org.orquestador.users.rest.utils.ResponseUtil;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto

    public Response getAllUsers() {
        List<Users> users = listAll(Sort.by("name"));
        return ResponseUtil.ok(users);
    }
    public Response getById(@PathParam("id") Long id) {
        Users user = findById(id);
        if (user == null) {
            throw ResponseUtil.notFoundException();
        }
        return ResponseUtil.ok(user);
    }

    public Response update(@PathParam("id") Long id, @Valid @RequestBody Users user) {
        Users existingUser = findByIdOptional(id)
                .orElseThrow(ResponseUtil::notFoundException);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        persist(existingUser);
        return ResponseUtil.ok(existingUser);
    }

    public Response delete(@PathParam("id") Long id) {
        Users user = findById(id);
        if (user == null) {
            throw ResponseUtil.notFoundException();
        }
        delete(user);
        return ResponseUtil.noContent();
    }

    public Users findByEmailAndPassword(String email, String password) {
        return find("email = ?1 and password = ?2", email, password).firstResult();
    }
}
