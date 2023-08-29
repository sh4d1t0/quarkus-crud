package org.orquestador.roles.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.response.utils.ResponseUtil;
import org.orquestador.roles.entities.Rol;
import org.orquestador.roles.repositories.RolRepository;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolApi {
    @Inject
    RolRepository rolRepository;

    @GET
    @Operation(summary = "Get all roles")
    @APIResponse(responseCode = "200", description = "Roles found")
    @APIResponse(responseCode = "404", description = "Roles not found")
    public Uni<Response> getAll() {
        return rolRepository.getAll()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Roles not found"));
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a role by id")
    @APIResponse(responseCode = "200", description = "Rol found")
    @APIResponse(responseCode = "404", description = "Rol not found")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return rolRepository.getById(id)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Rol not found"));
    }

    @POST
    @Operation(summary = "Create a new role")
    @APIResponse(responseCode = "201", description = "The created role")
    public Uni<Response> create(@Valid @RequestBody Rol rol) {
        if (rol == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return rolRepository.create(rol)
                .onItem().ifNotNull().transformToUni(ResponseUtil::created)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Rol con not be created"));
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing role")
    @APIResponse(responseCode = "201", description = "The updated role")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    @APIResponse(responseCode = "400", description = "Bad request")
    public Uni<Response> update(@PathParam("id") Long id, Rol rol) {
        if (rol == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return rolRepository.update(id, rol)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Rol not found!"));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an existing role")
    @APIResponse(responseCode = "204", description = "Role deleted")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return rolRepository.delete(id)
                .onItem().ifNotNull()
                .transformToUni(rol -> ResponseUtil.okWithMessage("Rol has been deleted."))
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Rol not found"));
    }
}
