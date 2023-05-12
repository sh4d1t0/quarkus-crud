package org.orquestador.users.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.entities.Users;
import org.orquestador.users.rest.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<Users, Long> {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    @Inject
    EntityManager entityManager;

    public Uni<List<Users>> getAllUsers() {
        log.info("Fetching all users");
        return Uni.createFrom().item(() -> {
                    List<Users> users = entityManager.createQuery("SELECT u FROM Users u", Users.class).getResultList();
                    return fetchRolesForUsers(users);
                })
                .onItem().transformToUni(users -> Uni.createFrom().item(users))
                .runSubscriptionOn(Infrastructure.getDefaultExecutor());
    }

    private List<Users> fetchRolesForUsers(List<Users> users) {
        List<Long> rolIds = users.stream()
                .map(user -> user.getRol().getId())
                .collect(Collectors.toList());

        List<Rol> roles = entityManager.createQuery("SELECT r FROM Rol r WHERE r.id IN :rolIds", Rol.class)
                .setParameter("rolIds", rolIds)
                .getResultList();

        Map<Long, Rol> rolMap = roles.stream()
                .collect(Collectors.toMap(Rol::getId, Function.identity()));

        for (Users user : users) {
            Long rolId = user.getRol().getId();
            Rol rol = rolMap.get(rolId);
            user.setRol(rol);
        }

        return users;
    }

    public Uni<Response> getById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return Uni.createFrom().item(() -> findById(id))
                .onItem().ifNotNull().transform(user -> Response.status(Response.Status.OK).entity(user).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build())
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    public Uni<Response> create(@Valid @RequestBody Users user) {
        log.info("Creating user: {}", user.getName());
        return Uni.createFrom().voidItem()
                .onItem().invoke(() -> persist(user))
                .map(ignore -> ResponseUtil.created(user))
                //.onFailure().recoverWithItem(error -> Response.status(Response.Status.BAD_REQUEST).build());
                .onFailure().recoverWithUni(error -> Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).build()));

    }

    public Uni<Response> update(Long id, @Valid @RequestBody Users user) {
        log.info("Updating user with ID: {}", id);
        return Uni.createFrom().item(() -> {
            Users existingUser = findByIdOptional(id)
                    .orElseThrow(ResponseUtil::notFoundException);
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());

            Rol existingRol = existingUser.getRol();
            Rol newRol = user.getRol();

            // Actualizar el campo del rol solo si ha cambiado
            if (existingRol != null && newRol != null && !existingRol.getId().equals(newRol.getId())) {
                existingUser.setRol(newRol);
            }

            persist(existingUser);
            return ResponseUtil.ok(existingUser);
        });
    }

    // Cambiar el tipo de retorno y agregar la anotación @Blocking
    @Transactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        log.info("Deleting user with ID: {}", id);
        Users user = findById(id);
        if (user == null) {
            return Uni.createFrom().item(ResponseUtil.notFound());
        }
        entityManager.remove(user);
        return Uni.createFrom().nullItem()
                .map(ignore -> ResponseUtil.noContent());
    }

    public Users findByEmailAndPassword(String email, String password) {
        return find("email = ?1 and password = ?2", email, password).firstResult();
    }
}
