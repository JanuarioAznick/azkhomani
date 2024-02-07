package com.az.khomani.dto;

import com.az.khomani.entities.User;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 3, max = 60, message = "O nome deve ter no mínimo 3 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;

    @Email(message = "Introduza um email válido")
    private String email;

    @Size(min = 13, max = 13, message = "O número de identificação deve ter no minimo 13 caracteres")
    private String idNumber;

    @PastOrPresent(message = "A data nao deve ser futura")
    private Date validate;

    @Size(min = 9, max = 9, message = "O número do NUIT deve ter 9 caracteres")
    @NotBlank(message = "Campo obrigatorio")
    private String nuit;

    @Size(min = 13, max = 13, message = "O número de telefone deve ter no minimo 9 caracteres")
    private String phone;
    private String license;
    private String address;
    private String imgUrl;

    @NotBlank(message = "Campo obrigatorio")
    private String alvaral;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String idNumber,Date validate,
                   String nuit, String phone, String license, String address, String imgUrl, String alvaral) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idNumber = idNumber;
        this.validate = validate;
        this.nuit = nuit;
        this.phone = phone;
        this.license = license;
        this.address = address;
        this.imgUrl = imgUrl;
        this.alvaral = alvaral;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        idNumber = entity.getIdNumber();
        validate = entity.getValidate();
        nuit = entity.getNuit();
        phone = entity.getPhone();
        license = entity.getLicense();
        address = entity.getAddress();
        imgUrl = entity.getImgUrl();
        alvaral = entity.getAlvaral();

        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getValidate() {
        return validate;
    }

    public void setValidate(Date validate) {
        this.validate = validate;
    }

    public String getNuit() {
        return nuit;
    }

    public void setNuit(String nuit) {
        this.nuit = nuit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAlvaral() {
        return alvaral;
    }

    public void setAlvaral(String alvaral) {
        this.alvaral = alvaral;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

}
