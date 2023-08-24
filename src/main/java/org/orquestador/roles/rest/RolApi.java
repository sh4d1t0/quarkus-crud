package org.orquestador.roles.rest;

import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.response.utils.ResponseUtil;
import org.orquestador.roles.entities.Rol;
import org.orquestador.roles.repositories.RolRepository;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolApi {
    @Inject
    RolRepository rolRepository;
    @Inject
    private Validator validator;

    @GET
    @Operation(summary = "Get all roles")
    @APIResponse(responseCode = "200", description = "Roles found")
    @APIResponse(responseCode = "404", description = "Roles not found")
    public Uni<Response> getAll() {
        return rolRepository.getAll()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(new WebApplicationException("Roles not found", Response.Status.NOT_FOUND));
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a role by id")
    @APIResponse(responseCode = "200", description = "Rol found")
    @APIResponse(responseCode = "404", description = "Rol not found")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return rolRepository.getById(id)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(new WebApplicationException("Rol not found", Response.Status.NOT_FOUND));
    }

    @POST
    @Operation(summary = "Create a new role")
    @APIResponse(responseCode = "201", description = "The created role")
    public Uni<Response> create(@Valid @RequestBody Rol rol) {
        if (rol == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return rolRepository.create(rol)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull()
                .failWith(new WebApplicationException("Rol con not be created", Response.Status.NOT_FOUND));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing role")
    @APIResponse(responseCode = "201", description = "The updated role")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    @APIResponse(responseCode = "400", description = "Bad request")
    public Uni<Response> update(@PathParam("id") Long id, Rol newRol) {
        if (newRol == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return rolRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingRol -> {
                    existingRol.setName(newRol.getName());
                    existingRol.setDescription(newRol.getDescription());

                    Set<ConstraintViolation<Rol>> violations = validator.validate(existingRol);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        return Uni.createFrom().item(ResponseUtil.badRequest(errorMessage));
                    }

                    return rolRepository.persistOrUpdate(existingRol)
                            .onItem().transform(updatedRol -> ResponseUtil.ok(updatedRol));
                })
                .flatMap(uni -> uni)
                .onItem().ifNull().switchTo(ResponseUtil.notFound("Role not found"));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an existing role")
    @APIResponse(responseCode = "204", description = "Role deleted")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return rolRepository.delete(id)
                .onItem().ifNotNull()
                .transformToUni(rol -> ResponseUtil.okWithMessagge("Rol has been deleted."))
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Rol not found"));
    }
}
