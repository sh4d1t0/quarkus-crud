package org.orquestador.response.mappers;

import org.orquestador.response.utils.ErrorResponse;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return Response.status(exception.getResponse().getStatus())
                .entity(errorResponse)
                .build();
    }
}
