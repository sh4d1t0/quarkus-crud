package org.orquestador.roles.repositories;

import java.util.List;
import java.util.function.Supplier;

import org.orquestador.roles.entities.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RolRepository implements PanacheRepositoryBase<Rol, Long> {
    private static final Logger log = LoggerFactory.getLogger(RolRepository.class);

    Supplier<Uni<List<Rol>>> sortedRolesSupplier = () -> this.listAllSorted("name");

    public Uni<List<Rol>> listAllSorted(String sortCriteria) {
        return Panache.withTransaction(() -> Rol.listAll(Sort.by(sortCriteria)));
    }

    public Uni<List<Rol>> getAll() {
        log.info("Fetching all roles by name");
        return sortedRolesSupplier.get();
    }

    public Uni<Rol> getById(Long id) {
        log.info("Fetching rol by ID: {}", id);
        return Panache.withTransaction(() -> Rol.findById(id));
    }

    public Uni<Rol> create(Rol rol) {
        log.info("Creating rol: {}", rol.getName());
        return Panache.withTransaction(rol::persist)
                .replaceWith(rol);
    }

    public Uni<Rol> update(Long id, Rol rol) {
        log.info("Updating rol with ID: {}", id);
        return Panache.withTransaction(() -> Rol.<Rol>findById(id)
                .onItem().ifNotNull().transformToUni((Rol existingRol) -> {
                    existingRol.setName(rol.getName());
                    existingRol.setDescription(rol.getDescription());
                    return existingRol.persistAndFlush().replaceWith(existingRol);
                }));
    }

    public Uni<Rol> persistOrUpdate(Rol rol) {
        return Panache.withTransaction(rol::persist)
                .replaceWith(rol);
    }

    public Uni<Rol> delete(Long id) {
        return Panache.withTransaction(() -> Rol.<Rol>findById(id)
                .onItem().ifNotNull().transformToUni(rol -> rol.delete().replaceWith(rol)));
    }

}
