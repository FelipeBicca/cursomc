package com.felipebicca.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.felipebicca.cursomc.domain.Cidade;
import com.felipebicca.cursomc.domain.Cliente;
import com.felipebicca.cursomc.domain.Endereco;
import com.felipebicca.cursomc.domain.enums.Perfil;
import com.felipebicca.cursomc.domain.enums.TipoCliente;
import com.felipebicca.cursomc.dto.ClienteDTO;
import com.felipebicca.cursomc.dto.ClienteNewDTO;
import com.felipebicca.cursomc.repositories.ClienteRepository;
import com.felipebicca.cursomc.repositories.EnderecoRepository;
import com.felipebicca.cursomc.security.UserSS;
import com.felipebicca.cursomc.services.exception.AuthorizationException;
import com.felipebicca.cursomc.services.exception.DataIntegrityException;
import com.felipebicca.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository endRepo;
	
	@Autowired
	private S3Services s3Services;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;

	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasHole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto n??o encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		repo.save(obj);
		endRepo.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("N??o ?? poss??vel excluir pois h?? pedidos relacionados.");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasHole(Perfil.ADMIN) && !email.equals(user.getEmail())) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		Cliente cli = repo.findByEmail(email);
		if(cli == null) {
			throw new ObjectNotFoundException("Objeto n??o encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		
		return cli;
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {

		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO obj) {
		Cliente cli = new Cliente(null, obj.getNome(), obj.getEmail(), obj.getCpfOuCnpj(),
				TipoCliente.toEnum(obj.getTipoCliente()), bCryptPasswordEncoder.encode(obj.getSenha()));
		
		Cidade cid = new Cidade(obj.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null, obj.getLogradouro(), obj.getNumero(), obj.getComplemento(), obj.getBairro(),
				obj.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(obj.getTelefone1());

		if (obj.getTelefone2() != null) {
			cli.getTelefones().add(obj.getTelefone2());
		}

		if (obj.getTelefone3() != null) {
			cli.getTelefones().add(obj.getTelefone3());
		}
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		
		if(user == null) {
			throw new AuthorizationException("Acesso Negado!");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		jpgImage = ImageService.RotateImage(jpgImage, 90);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Services.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}