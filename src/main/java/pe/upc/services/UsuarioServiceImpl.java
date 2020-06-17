package pe.upc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.entities.Perfil;
import pe.upc.entities.Usuario;
import pe.upc.repository.PerfilRepository;
import pe.upc.repository.UsuarioRepository;

@Service //siempre tiene la capa de servicios/negocio
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;    
	@Autowired
	private PerfilRepository perfilRepositorio;

	@Override
	@Transactional
	public Usuario insert(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	@Override
	@Transactional
	public Usuario insert(int idUsuario, int idPerfil) {
		Usuario usuario = usuarioRepository.findById(idUsuario).get();
		Perfil perfil = perfilRepositorio.findById(idPerfil).get();		
		if(usuario!=null && perfil!=null)
			return usuarioRepository.save(usuario);
		else
			return null;
	}

	@Override
	public List<Usuario> list() {
		return (List<Usuario>)usuarioRepository.findAll();
	}

	@Override
	public Usuario search(Integer id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario invalido"));
	}

	@Override
	public List<Usuario> findByName(String name) {
		return usuarioRepository.findByNombre(name);
	}

	@Override
	@Transactional
	public void delete(Usuario usuario) {
		usuarioRepository.delete(usuario);		
	}

}
