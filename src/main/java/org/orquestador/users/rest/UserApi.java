package org.orquestador.users.rest;

import io.smallrye.common.annotation.Blocking;
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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.orquestador.roles.entities.Rol;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;
import org.orquestador.users.rest.utils.ResponseUtil;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Resource", description = "Endpoints for handling and managing user related operations.")
public class UserApi {
    @Inject
    UserRepository userRepository;

    @GET
    @Operation(summary = "Get all users")
    public Uni<Response> getAll() {
        return userRepository.getAllUsers()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(ResponseUtil::notFoundException);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a user by id")
    @APIResponse(responseCode = "200", description = "User founded")
    @APIResponse(responseCode = "404", description = "The user does not exist")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return userRepository.getById(id);
    }

    // Cambiar el tipo de retorno y agregar la anotación @Transactional
    @POST
    @Transactional
    @Operation(summary = "Create a new user")
    @APIResponse(responseCode = "201", description = "The created user")
    public Uni<Response> create(@Valid Users user) {
        Rol rol = user.getRol();
        if (rol == null || rol.getId() == null) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("The 'id' field of 'rol' is required.").build());
        }

        return userRepository.create(user);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Blocking // Agregar esta anotación a nivel de clase
    @Operation(summary = "Update an existing user")
    @APIResponse(responseCode = "200", description = "User updated")
    @APIResponse(responseCode = "400", description = "Bad request")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> update(@PathParam("id") Long id, @Valid Users user) {
        return userRepository.update(id, user);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Blocking // Agregar esta anotación a nivel de clase
    @Operation(summary = "Delete an existing user")
    @APIResponse(responseCode = "204", description = "User deleted")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return userRepository.delete(id);
    }
}
