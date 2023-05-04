package org.orquestador.users.graphql;

import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;

import java.util.List;

@GraphQLApi
public class CustomerResource {
    @Inject
    UserRepository userRepository;

    @Query("allUsers")
    @Description("List all users")
    public List<Users> getAllUsers() {
        return userRepository.listAll();
    }

    @Query("user")
    @Description("Obtain a user")
    public Users getUser(@Name("userId") Long Id) {
        return userRepository.findById(Id);
    }
}
