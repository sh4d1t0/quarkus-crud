package org.orquestador.users.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.response.utils.ErrorResponse;
import org.orquestador.response.utils.ResponseUtil;
import org.orquestador.roles.entities.Rol;
import org.orquestador.roles.repositories.RolRepository;
import org.orquestador.users.dtos.UserDTO;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("/users")
@ApplicationScoped // or @RequestScoped
public class UserApi {
    @Inject
    UserRepository userRepository;
    @Inject
    RolRepository rolRepository;

    @GET
    @Operation(summary = "Get all users")
    public Uni<Response> getAll() {
        return userRepository.getAllUsers()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(new WebApplicationException("Users not found", Response.Status.NOT_FOUND));
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a user by id")
    @APIResponse(responseCode = "200", description = "User found")
    @APIResponse(responseCode = "404", description = "The user does not exist")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return userRepository.getById(id)
                .onItem().ifNotNull().transform(this::toResponseDTO)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(new WebApplicationException("User not found", Response.Status.NOT_FOUND));
    }

    private UserDTO toResponseDTO(Users user) {
        UserDTO model = new UserDTO();
        model.setId(user.getId());
        model.setName(user.getName());
        model.setEmail(user.getEmail());
        model.setRoleName(user.getRol().getName());
        return model;
    }

    @GET
    @Path("rol/{id}")
    @Operation(summary = "Get users by rol")
    @APIResponse(responseCode = "200", description = "Users found")
    @APIResponse(responseCode = "404", description = "The users in rol not found")
    public Uni<Response> getUsersByRol(@PathParam("id") Long rolId) {
        return userRepository.findByRoleId(rolId)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull()
                .failWith(new WebApplicationException("Users in rol not found", Response.Status.NOT_FOUND));
    }

    @POST
    @Operation(summary = "Create a new user")
    @APIResponse(responseCode = "201", description = "The created user")
    public Uni<Response> create(@Valid @RequestBody Users user) {
        if (user == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }

        Rol rol = user.getRol();
        if (rol == null || rol.getId() == null) {
            return ResponseUtil.badRequest("The 'id' field of 'rol' is required.");
        }

        return rolRepository.findById(rol.getId())
                .onItem().ifNull()
                .failWith(() -> ResponseUtil.badRequestException("Rol not found"))
                .replaceWith(user)
                .onItem().transformToUni(userRepository::create)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("User not found"));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing user")
    @APIResponse(responseCode = "200", description = "User updated")
    @APIResponse(responseCode = "400", description = "Bad request")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> update(@PathParam("id") Long id, @Valid Users user) {
        if (user == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }

        return userRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setRol(user.getRol()); // Update rol
                    return userRepository.update(id, existingUser);
                })
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onFailure().recoverWithUni(() -> ResponseUtil.badRequest("Internal Server Error"))
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("User not found"));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an existing user")
    @APIResponse(responseCode = "200", description = "User deleted")
    @APIResponse(responseCode = "404", description = "User not found")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return userRepository.delete(id)
                .onItem().ifNotNull()
                .transformToUni(user -> ResponseUtil.ok(new ErrorResponse("User has been deleted.")))
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("User not found"));
    }
}
