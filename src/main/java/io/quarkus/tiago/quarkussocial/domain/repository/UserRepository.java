package io.quarkus.tiago.quarkussocial.domain.repository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.tiago.quarkussocial.domain.model.User;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}