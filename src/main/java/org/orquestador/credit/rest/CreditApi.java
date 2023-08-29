package org.orquestador.credit.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.credit.entities.Credit;
import org.orquestador.credit.repositories.CreditRepository;
import org.orquestador.response.utils.ResponseUtil;

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

@Path("/credit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreditApi {
    @Inject
    CreditRepository creditRepository;

    @GET
    @Path("/prospects")
    @Operation(summary = "Get All credits")
    @APIResponse(responseCode = "200", description = "Credits found")
    @APIResponse(responseCode = "404", description = "Credits not found")
    public Uni<Response> getAll() {
        return creditRepository.getAll()
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Credits not found"));
    }

    @GET
    @Path("/{id}/prospect")
    @Operation(summary = "Get a credit by id")
    @APIResponse(responseCode = "200", description = "Credits found")
    @APIResponse(responseCode = "404", description = "Credits not found")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return creditRepository.getById(id)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Credit not found"));
    }

    @POST
    @Operation(summary = "Create a new credit")
    @APIResponse(responseCode = "201", description = "The created credit")
    public Uni<Response> create(@Valid @RequestBody Credit credit) {
        if (credit == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return creditRepository.create(credit)
                .onItem().ifNotNull().transformToUni(ResponseUtil::ok)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Credit can not be created"));
    }

    @PUT
    @Path("/{id}/prospect")
    @Operation(summary = "Update an existing credit")
    @APIResponse(responseCode = "201", description = "The updated credit")
    @APIResponse(responseCode = "404", description = "The credit does not exist")
    @APIResponse(responseCode = "400", description = "Bad request")
    public Uni<Response> update(@PathParam("id") Long id, Credit credit) {
        if (credit == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid request body"));
        }
        return creditRepository.update(id, credit)
                .onItem().ifNotNull().transformToUni(ResponseUtil::created)
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Credit not found"));
    }

    @DELETE
    @Path("/{id}/prospect")
    @Operation(summary = "Delete an existing credit")
    @APIResponse(responseCode = "204", description = "Credit deleted")
    @APIResponse(responseCode = "404", description = "The credit does not exist")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return creditRepository.delete(id)
                .onItem().ifNotNull()
                .transformToUni(credit -> ResponseUtil.okWithMessage("Credit has been deleted."))
                .onItem().ifNull().switchTo(() -> ResponseUtil.notFound("Credit not found"));
    }
}
