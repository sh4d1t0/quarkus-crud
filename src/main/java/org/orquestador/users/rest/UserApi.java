package org.orquestador.users.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserApi {
    @Inject
    UserRepository userRepository;

    @GET
    @Operation(summary = "Get all users")
    public Response getAll() {
        return userRepository.getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a user by id")
    @APIResponse(responseCode = "200", description = "User founded")
    @APIResponse(responseCode = "404", description = "The user does not exist")
    public Response getById(@PathParam("id") Long id) {
        return userRepository.getById(id);
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new user")
    @APIResponse(responseCode = "201", description = "The created user")
    public Response create(@Valid @RequestBody Users user) {
        return userRepository.create(user);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update an existing user")
    @APIResponse(responseCode = "200", description = "User updated")
    @APIResponse(responseCode = "400", description = "Bad request")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response update(@PathParam("id") Long id, @Valid @RequestBody Users user) {
        return userRepository.update(id, user);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete an existing user")
    @APIResponse(responseCode = "204", description = "User deleted")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response delete(@PathParam("id") Long id) {
        return userRepository.delete(id);
    }
}
