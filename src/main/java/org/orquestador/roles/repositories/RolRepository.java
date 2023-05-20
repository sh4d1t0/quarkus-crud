package org.orquestador.roles.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.rest.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

@ApplicationScoped
public class RolRepository implements PanacheRepository<Rol> {
    private static final Logger log = LoggerFactory.getLogger(RolRepository.class);
    @Inject
    EntityManager entityManager;
    /**
     * sortedRolesSupplier es una variable de tipo Supplier<List<Rol>>.
     * El tipo Supplier es una interfaz funcional que representa una función que no toma ningún argumento pero devuelve un valor,
     * en este caso, una lista de roles ordenada por nombre.
     * */
    Supplier<List<Rol>> sortedRolesSupplier = () -> this.listAllSorted("name");

    /**
     * Método listAllSorted para aceptar un String.
     * Luego, en el método, se llama a Sort.by().
     * */
    public List<Rol> listAllSorted(String sortCriteria) {
        return this.listAll(Sort.by(sortCriteria));
    }

    /**
     * Método getAll para recuperar todos los roles.
     * se pasa la lista ordenada con el criterio del Supplier: item(sortedRolesSupplier)
     * en caso de no querer ordenamiento simplemente se hace referencia al listall(): item(this::listAll)
     * */
    public Uni<List<Rol>> getAll() {
        log.info("Fetching all roles by name");
        return Uni.createFrom().item(sortedRolesSupplier.get())
                .onItem().ifNotNull().transform(roles -> roles)
                .onItem().ifNull().failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    public Uni<Rol> getById(Long id) {
        log.info("Fetching rol by ID: {}", id);
        return Uni.createFrom().item(() -> findById(id))
                .onItem().ifNotNull().transform(rol -> rol)
                .onItem().ifNull().failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    public Uni<Rol> create(Rol rol) {
        log.info("Creating rol: {}", rol.getName());
        return Uni.createFrom().item(() -> {
                    persistAndFlush(rol);
                    return rol;
                })
                .onItem().ifNotNull().transform(createdRol -> createdRol)
                .onItem().ifNull().failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    public Uni<Rol> update(Long id, Rol rol) {
        log.info("Updating rol with ID: {}", id);
        return Uni.createFrom().item(() -> findById(id))
                .onItem().ifNotNull().transformToUni(existingRol -> {
                    existingRol.setName(rol.getName());
                    existingRol.setDescription(rol.getDescription());
                    persistAndFlush(existingRol);
                    return Uni.createFrom().item(existingRol);
                })
                .onItem().ifNull().failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    public Uni<Response> delete(@PathParam("id") Long id) {
        log.info("Deleting rol with ID: {}", id);
        return Uni.createFrom().item(() -> findByIdOptional(id))
                .onItem().transformToUni(optionalRol -> {
                    if(optionalRol.isPresent()){
                        Rol rol = optionalRol.get();
                        entityManager.remove(rol);
                        return ResponseUtil.noContent();
                    } else {
                        return ResponseUtil.notFound();
                    }
                });
    }

}
