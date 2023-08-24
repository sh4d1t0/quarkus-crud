package org.orquestador.users.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@GraphQLApi
public class CustomerResource {
    @Inject
    UserRepository userRepository;

    @Query("allUsers")
    @Description("List all users")
    @WithSession
    public Uni<List<Users>> getAllUsers() {
        return userRepository.listAll();
    }

    @Query("user")
    @Description("Obtain a user")
    @WithSession
    public Uni<Users> getUser(@Name("userId") Long Id) {
        return userRepository.findById(Id);
    }
}
