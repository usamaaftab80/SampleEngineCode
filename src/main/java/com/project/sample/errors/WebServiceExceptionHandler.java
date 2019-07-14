package com.project.sample.errors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public final class WebServiceExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {
    	
    	Status status = Status.INTERNAL_SERVER_ERROR;
		if (exception instanceof NotAuthorizedException) {
			status = Status.UNAUTHORIZED;
		} else if (exception instanceof ForbiddenException) {
    		status = Status.FORBIDDEN;
    	} else if (exception instanceof NotFoundException) {
    		status = Status.NOT_FOUND;
    	} else if (exception instanceof BadRequestException) {
    		status = Status.BAD_REQUEST;
    	}
    	
        return Response.status(status)
        		.entity(new ErrorMessage(exception.getMessage()))
        		.type(MediaType.APPLICATION_JSON).build();

    }
}
