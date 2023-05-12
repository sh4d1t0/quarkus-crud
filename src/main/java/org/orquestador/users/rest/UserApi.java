package org.orquestador.users.rest;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;
import org.orquestador.users.rest.utils.ResponseUtil;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserApi {
    @Inject
    UserRepository userRepository;

    // Cambiar el tipo de retorno y agregar la anotación @Blocking
    @GET
    @Operation(summary = "Get all users")
    public Uni<Response> getAll() {
        return userRepository.getAllUsers()
                .onItem().transform(ResponseUtil::ok);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a user by id")
    @APIResponse(responseCode = "200", description = "User founded")
    @APIResponse(responseCode = "404", description = "The user does not exist")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return userRepository.getById(id)
                .onItem().transformToUni(response -> {
                    if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                        return Uni.createFrom().item(ResponseUtil.notFound());
                    } else {
                        return Uni.createFrom().item(response);
                    }
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    // Cambiar el tipo de retorno y agregar la anotación @Transactional
    @POST
    @Transactional
    @Operation(summary = "Create a new user")
    @APIResponse(responseCode = "201", description = "The created user")
    public Uni<Response> create(@Valid @RequestBody Users user) {
        Rol rol = user.getRol();
        if (rol == null || rol.getId() == null) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("The 'id' field of 'rol' is required.").build());
        }

        return userRepository.create(user);
    }

    // Cambiar el tipo de retorno y agregar la anotación @Transactional
    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update an existing user")
    @APIResponse(responseCode = "200", description = "User updated")
    @APIResponse(responseCode = "400", description = "Bad request")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> update(@PathParam("id") Long id, @Valid @RequestBody Users user) {
        return userRepository.update(id, user)
                .onFailure().recoverWithItem(error -> ResponseUtil.badRequest());
    }

    // Cambiar el tipo de retorno y agregar la anotación @Transactional
    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete an existing user")
    @APIResponse(responseCode = "204", description = "User deleted")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Uni.createFrom().item(() -> userRepository.delete(id).await())
                .onItem().transform(ignore -> ResponseUtil.noContent())
                .onItem().ifNull().continueWith(ResponseUtil::notFound);
    }
}
