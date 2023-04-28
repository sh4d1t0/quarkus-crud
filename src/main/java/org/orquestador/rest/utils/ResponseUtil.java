package org.orquestador.rest.utils;

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
        return Response.noContent().build();
    }

    public static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static WebApplicationException notFoundException() {
        return new WebApplicationException(ResponseUtil.notFound());
    }

    // otros métodos aquí...
}
