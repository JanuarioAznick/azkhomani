package com.az.khomani.services.validation;

import com.az.khomani.dto.UserInsertDTO;
import com.az.khomani.entities.User;
import com.az.khomani.repositories.UserRepository;
import com.az.khomani.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann){
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context){

        List<FieldMessage> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail());

        if(user != null){
           list.add(new FieldMessage("email", "O email já existe!"));
        }

        for(FieldMessage e : list){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}
