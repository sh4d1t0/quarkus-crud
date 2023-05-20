package org.orquestador.users.rest.utils;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ResponseUtil {
    public static Uni<Response> ok(Object entity) {
        return Uni.createFrom().item(() -> Response.ok(entity).build());
    }

    public static Uni<Response> created(Object entity) {
        return Uni.createFrom().item(() -> Response.status(Response.Status.CREATED).entity(entity).build());
    }

    public static Uni<Response> noContent() {
        return Uni.createFrom().item(() -> Response.status(Response.Status.NO_CONTENT).build());
    }

    /*public static Uni<Response> notFound(Object entity) {
        return Uni.createFrom().item(() -> Response.status(Response.Status.NOT_FOUND).entity(entity).build());
    }
    public static WebApplicationException notFoundException() {
        return new WebApplicationException(Response.Status.NOT_FOUND);
    }*/
    public static Uni<Response> notFound() {
        return Uni.createFrom().item(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
    /*public static Uni<Response> notFoundException() {
        return Uni.createFrom().failure(new WebApplicationException(Response.Status.NOT_FOUND));
    }*/
    public static WebApplicationException notFoundException() {
        return new WebApplicationException(Response.Status.NOT_FOUND);
    }


    public static Uni<Response> badRequest() {
        return Uni.createFrom().item(() -> Response.status(Response.Status.BAD_REQUEST).build());
    }
    public static WebApplicationException badRequestException() {
        return new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    public static Uni<Response> unauthorized() {
        return Uni.createFrom().item(() -> Response.status(Response.Status.UNAUTHORIZED).build());
    }
    public static WebApplicationException unauthorizedException() {
        return new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // otros métodos aquí...
}
