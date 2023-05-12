package org.orquestador.users.rest.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ResponseUtil {
    public static Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public static Response created(Object entity) {
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    public static Response noContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public static Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    public static WebApplicationException unauthorizedException() {
        return new WebApplicationException(org.orquestador.users.rest.utils.ResponseUtil.unauthorized());
    }

    public static WebApplicationException notFoundException() {
        return new WebApplicationException(org.orquestador.users.rest.utils.ResponseUtil.notFound());
    }

    // otros métodos aquí...
}
