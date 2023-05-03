package org.orquestador.login.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.orquestador.login.entities.Login;
import org.orquestador.users.entities.Users;
import org.orquestador.users.repositories.UserRepository;
import org.orquestador.users.rest.utils.ResponseUtil;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginApi {
    @Inject
    UserRepository userRepository;

    @POST
    @Transactional
    @Operation(summary = "Login a user")
    @APIResponse(responseCode = "200", description = "Login successful")
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    public Response login(@Valid @RequestBody Login login) {
        Users foundUser = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword());

        if (foundUser == null) {
            throw ResponseUtil.unauthorizedException();
        }
        return ResponseUtil.ok(foundUser);
    }
}
