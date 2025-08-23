package pauloh.main.adapter.output.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pauloh.main.adapter.output.repository.entity.UserEntity;

@Repository
public interface UserRepository {
  UserEntity save(UserEntity userEntity);

  Optional<UserEntity> findById(Long id);

  List<UserEntity> findAll();
}
