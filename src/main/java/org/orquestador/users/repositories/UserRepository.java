package org.orquestador.users.repositories;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.orquestador.roles.entities.Rol;
import org.orquestador.users.entities.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<Users, Long> {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public Uni<List<Users>> getAllUsers() {
        log.info("Get All Users");
        return Panache.withTransaction(() -> find("SELECT u FROM Users u").list()
                .onItem().transformToUni(this::fetchRolesForUsers));
    }

    private Uni<List<Users>> fetchRolesForUsers(List<Users> users) {
        log.info("Fetching roles for users");
        List<Long> rolIds = users.stream()
                .map(user -> user.getRol().getId())
                .collect(Collectors.toList());

        return Panache.withTransaction(
                () -> Rol.find("SELECT r FROM Rol r WHERE r.id IN :rolIds", Parameters.with("rolIds", rolIds)).list()
                        .onItem().transform(roles -> {
                            Map<Long, Rol> rolMap = roles.stream()
                                    .map(rol -> (Rol) rol)
                                    .collect(Collectors.toMap(Rol::getId, Function.identity()));

                            for (Users user : users) {
                                Long rolId = user.getRol().getId();
                                Rol rol = rolMap.get(rolId);
                                user.setRol(rol);
                            }

                            return users;
                        }));
    }

    public Uni<Users> getById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return Panache.withTransaction(() -> Users.findById(id));
    }

    public Uni<List<Users>> findByRoleId(Long rolId) {
        log.info("Fetching users by Rol ID: {}", rolId);
        return Panache.withTransaction(() -> Users.find("rol.id", rolId).list());
    }

    public Uni<Users> create(Users user) {
        return Panache.withTransaction(() -> persist(user))
                .replaceWith(user);
    }

    public Uni<Users> updateProperties(Users existingUser, Users user) {
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRol(user.getRol());
        return existingUser.persistAndFlush().replaceWith(existingUser);
    }

    public Uni<Users> update(Long id, Users user) {
        log.info("Updating user with ID: {}", id);
        return Panache.withTransaction(() -> Users.<Users>findById(id)
                .onItem().ifNotNull().transformToUni(existingUser -> updateProperties(existingUser, existingUser)));
    }

    public Uni<Users> delete(Long id) {
        return Panache.withTransaction(() -> Users.<Users>findById(id)
                .onItem().ifNotNull().transformToUni(user -> user.delete().replaceWith(user)));
    }

    public Uni<Users> findByEmailAndPassword(String email, String password) {
        return Panache.withTransaction(() -> Users.find("email = ?1 and password = ?2", email, password).firstResult());
    }

}
