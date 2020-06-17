package pe.upc.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.upc.entities.Usuario;
@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
  List<Usuario> findByNombre(String name);
}
