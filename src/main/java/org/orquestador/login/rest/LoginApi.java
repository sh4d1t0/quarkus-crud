package org.orquestador.login.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.login.entities.Login;
import org.orquestador.response.utils.ResponseUtil;
import org.orquestador.users.repositories.UserRepository;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginApi {
    @Inject
    UserRepository userRepository;

    @POST
    @Operation(summary = "Login a user")
    @APIResponse(responseCode = "200", description = "Login successful")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    public Uni<Response> login(@Valid @RequestBody Login login) {
        return userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .onItem().ifNotNull()
                .transformToUni(user -> ResponseUtil.accepted("Successful"))
                .onItem().ifNull().switchTo(() -> ResponseUtil.unauthorized("Unauthorized"));
    }
}
