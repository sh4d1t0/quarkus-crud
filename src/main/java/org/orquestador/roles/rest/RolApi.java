package org.orquestador.roles.rest;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
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
import org.orquestador.roles.repositories.RolRepository;
import org.orquestador.users.rest.utils.ResponseUtil;

import java.util.List;

@Path("/rol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolApi {
    @Inject
    RolRepository rolRepository;

    @GET
    @Blocking // Agregar esta anotación
    @Operation(summary = "Get all roles")
    @APIResponse(responseCode = "200", description = "Roles founded")
    @APIResponse(responseCode = "404", description = "The roles does not exist")
    public Uni<Response> getAll() {
        return rolRepository.getAll()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().failWith(ResponseUtil::notFoundException);
    }
    /*
    public Uni<Response> getAll() {
        return rolRepository.getAll()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().continueWith(() -> ResponseUtil.notFound().await().indefinitely());

    }
     */

    @GET
    @Path("/{id}")
    @Blocking // Agregar esta anotación a nivel de clase
    @Operation(summary = "Get a role by id")
    @APIResponse(responseCode = "200", description = "Role founded")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    public Uni<Rol> getById(@PathParam("id") Long id) {
        return rolRepository.getById(id);
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new role")
    @APIResponse(responseCode = "201", description = "The created role")
    public Uni<Rol> create(@Valid @RequestBody Rol rol) {
        return rolRepository.create(rol);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Blocking // Agregar esta anotación a nivel de clase
    @Operation(summary = "Update an existing role")
    @APIResponse(responseCode = "201", description = "The updated role")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    @APIResponse(responseCode = "400", description = "Bad request")
    public Uni<Rol> update(@PathParam("id") Long id, Rol rol) {
        return rolRepository.update(id, rol);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Blocking // Agregar esta anotación a nivel de clase
    @Operation(summary = "Delete an existing role")
    @APIResponse(responseCode = "204", description = "Role deleted")
    @APIResponse(responseCode = "404", description = "The role does not exist")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return rolRepository.delete(id);
    }
}
