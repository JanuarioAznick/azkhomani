package com.az.khomani.services;

import com.az.khomani.config.authentication.AuthenticationRequest;
import com.az.khomani.config.authentication.AuthenticationResponse;
import com.az.khomani.dto.RoleDTO;
import com.az.khomani.dto.UserDTO;
import com.az.khomani.dto.UserInsertDTO;
import com.az.khomani.dto.UserUpdateDTO;
import com.az.khomani.entities.Role;
import com.az.khomani.entities.User;
import com.az.khomani.repositories.RoleRepository;
import com.az.khomani.repositories.UserRepository;
import com.az.khomani.services.exceptions.DatabaseException;
import com.az.khomani.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("ENTITY NOT FOUND"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO saveUser(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);

        var jwtToken = jwtService.generateToken(entity);

         AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO dto) {
        try{
            User entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            var jwtToken = jwtService.generateToken(entity);

            AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
            return new UserDTO(entity);
        }
        catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found "+id);
        }
    }

    public void deleteUser(Long id) {
        try{
            repository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found "+id);
        }
        catch(DataIntegrityViolationException e){
            throw new DatabaseException("Database Violated");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setIdNumber(dto.getIdNumber());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setNuit(dto.getNuit());
        entity.setAlvaral(dto.getAlvaral());
        entity.setLicense(dto.getLicense());
        entity.setImgUrl(dto.getImgUrl());
        entity.setValidate(dto.getValidate());

        entity.getRoles().clear();
        for(RoleDTO rolDto : dto.getRoles()){
            Role role = roleRepository.getOne(rolDto.getId());
            entity.getRoles().add(role);
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UsernameNotFoundException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );

        var user = repository.findByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if(user == null) {
            logger.error("user not found "+username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("user found "+username);
        return user;
    }
}
