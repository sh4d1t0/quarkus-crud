package org.orquestador.response.utils;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ResponseUtil {
    private static ErrorResponse createErrorResponse(String message) {
        return new ErrorResponse(message);
    }

    private static Uni<Response> generateResponse(Response.Status status, String message) {
        ErrorResponse errorResponse = createErrorResponse(message);
        return Uni.createFrom().item(() -> Response.status(status).entity(errorResponse).build());
    }

    public static Uni<Response> ok(Object entity) {
        return Uni.createFrom().item(() -> Response.ok(entity).build());
    }

    public static Uni<Response> okWithMessage(String message) {
        return generateResponse(Response.Status.OK, message);
    }

    public static Uni<Response> created(Object entity) {
        return Uni.createFrom().item(() -> Response.status(Response.Status.CREATED).entity(entity).build());
    }

    public static Uni<Response> noContent(String message) {
        return generateResponse(Response.Status.NO_CONTENT, message);
    }

    public static Uni<Response> notFound(String message) {
        return generateResponse(Response.Status.NOT_FOUND, message);
    }

    public static WebApplicationException notFoundException(String message) {
        return new WebApplicationException(generateResponse(Response.Status.NOT_FOUND, message).await().indefinitely());
    }

    public static Uni<Response> badRequest(String message) {
        return generateResponse(Response.Status.BAD_REQUEST, message);
    }

    public static WebApplicationException badRequestException(String message) {
        return new WebApplicationException(
                generateResponse(Response.Status.BAD_REQUEST, message).await().indefinitely());
    }

    public static Uni<Response> accepted(String message) {
        return generateResponse(Response.Status.ACCEPTED, message);
    }

    public static Uni<Response> unauthorized(String message) {
        return generateResponse(Response.Status.UNAUTHORIZED, message);
    }

    public static WebApplicationException unauthorizedException(String message) {
        return new WebApplicationException(
                generateResponse(Response.Status.UNAUTHORIZED, message).await().indefinitely());
    }

    // otros métodos aquí...
}
