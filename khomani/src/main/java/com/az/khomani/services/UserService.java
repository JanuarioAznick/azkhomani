package com.az.khomani.services;

import com.az.khomani.dto.RoleDTO;
import com.az.khomani.dto.UserDTO;
import com.az.khomani.dto.UserInsertDTO;
import com.az.khomani.entities.Role;
import com.az.khomani.entities.User;
import com.az.khomani.repositories.RoleRepository;
import com.az.khomani.repositories.UserRepository;
import com.az.khomani.services.exceptions.DatabaseException;
import com.az.khomani.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

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
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        try{
            User entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
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
        entity.setAlvara(dto.getAlvara());
        entity.setLicenseUrl(dto.getLicenseUrl());
        entity.setImgUrl(dto.getImgUrl());
        entity.setValidate(dto.getValidate());
        entity.setUsername(dto.getUsername());

        entity.getRoles().clear();
        for(RoleDTO rolDto : dto.getRoles()){
            Role role = roleRepository.getOne(rolDto.getId());
            entity.getRoles().add(role);
        }

    }
}
