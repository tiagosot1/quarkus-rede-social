package io.quarkus.tiago.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.tiago.quarkussocial.domain.model.User;
import io.quarkus.tiago.quarkussocial.domain.repository.UserRepository;
import io.quarkus.tiago.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.tiago.quarkussocial.rest.dto.ResponseError;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Validator;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository repository;
    private final Validator validator;
    @Inject
    public UserResource(UserRepository repository,  Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser( CreateUserRequest userRequest ){
        Set<ConstraintViolation<Object>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user);

        return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }
    @GET
    @Path("{id}")
    public Response listUser(@PathParam("id") Long id){
        User user = repository.findById(id);
        if(user != null){
            return Response.ok(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser( @PathParam("id") Long id){
        User user = repository.findById(id);

        if(user != null){
            repository.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser( @PathParam("id") Long id, CreateUserRequest userData ){
        User user = repository.findById(id);

        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
