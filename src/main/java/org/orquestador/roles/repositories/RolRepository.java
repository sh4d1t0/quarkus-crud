package org.orquestador.roles.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.rest.utils.ResponseUtil;

import java.util.List;

@ApplicationScoped
public class RolRepository implements PanacheRepository<Rol> {
    // Implementación vacía porque PanacheRepository proporciona métodos por defecto
    public Response getAll() {
        List<Rol> roles = listAll(Sort.by("name"));
        return ResponseUtil.ok(roles);
    }

    public Response getById(Long id) {
        Rol rol = findById(id);
        if (rol == null) {
            throw ResponseUtil.notFoundException();
        }
        return ResponseUtil.ok(rol);
    }

    public Response create(Rol role) {
        persist(role);
        return ResponseUtil.created(role);
    }

    public Response update(Long id, Rol rol) {
        Rol existingRol = findByIdOptional(id)
                .orElseThrow(ResponseUtil::notFoundException);
        existingRol.setName(rol.getName());
        existingRol.setDescription(rol.getDescription());
        persist(existingRol);
        return ResponseUtil.ok(existingRol);
    }

    public Response delete(Long id) {
        Rol rol = findById(id);
        if (rol == null) {
            throw ResponseUtil.notFoundException();
        }
        delete(rol);
        return ResponseUtil.noContent();
    }
}
