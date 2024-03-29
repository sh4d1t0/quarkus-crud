package org.orquestador.roles.repositories;

import java.util.List;
import java.util.function.Supplier;

import lombok.NonNull;
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

    // NOTE This Supplier is for sortCriteria
    Supplier<Uni<List<Rol>>> sortedRolesSupplier = () -> this.listAllSorted("name");

    // NOTE This function obtain the list with sortCriteria in Supplier
    public Uni<List<Rol>> listAllSorted(String sortCriteria) {
        return Panache.withTransaction(() -> Rol.listAll(Sort.by(sortCriteria)));
    }

    // NOTE This function obtain a list using the Supplier
    public Uni<List<Rol>> getAll() {
        log.info("Fetching all roles by name");
        return sortedRolesSupplier.get();
    }

    public Uni<Rol> getById(Long id) {
        log.info("Fetching rol by ID: {}", id);
        return Panache.withTransaction(() -> Rol.findById(id));
    }

    public Uni<Rol> create(@NonNull Rol rol) {
        log.info("Creating rol: {}", rol.getName());
        return Panache.withTransaction(rol::persist)
                .replaceWith(rol);
    }

    public Uni<Rol> updateProperties(@NonNull Rol existingRol, @NonNull Rol rol) {
        existingRol.setName(rol.getName());
        existingRol.setDescription(rol.getDescription());
        return existingRol.persistAndFlush().replaceWith(existingRol);
    }

    public Uni<Rol> update(Long id, Rol rol) {
        log.info("Updating rol with ID: {}", id);
        return Panache.withTransaction(() -> Rol.<Rol>findById(id)
                .onItem().ifNotNull().transformToUni(existingRol -> updateProperties(existingRol, rol)));
    }

    public Uni<Rol> delete(Long id) {
        log.info("Delete rol with ID: {}", id);
        return Panache.withTransaction(() -> Rol.<Rol>findById(id)
                .onItem().ifNotNull().transformToUni(rol -> rol.delete().replaceWith(rol)));
    }

}
