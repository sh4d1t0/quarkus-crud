package org.orquestador.users.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.entities.Users;
import org.orquestador.users.rest.utils.ResponseUtil;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<Users, Long> {
    @Inject
    EntityManager entityManager;
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto

    public Response getAllUsers() {
        List<Users> users = findAll(Sort.by("name")).list();
        for (Users user : users) {
            Rol rol = entityManager.find(Rol.class, user.getRol().getId());
            user.setRol(rol);
        }
        return ResponseUtil.ok(users);
    }

    public Response getById(@PathParam("id") Long id) {
        Users user = findById(id);
        if (user == null) {
            throw ResponseUtil.notFoundException();
        }
        return ResponseUtil.ok(user);
    }

    public Response create(@Valid @RequestBody Users user) {
        persist(user);
        return ResponseUtil.created(user);
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
