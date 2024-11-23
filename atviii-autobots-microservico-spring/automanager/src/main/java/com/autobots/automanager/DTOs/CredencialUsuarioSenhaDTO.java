// src/main/java/com/autobots/automanager/dtos/CredencialUsuarioSenhaDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredencialUsuarioSenhaDTO extends CredencialDTO {
    private String nomeUsuario;
    private String senha;
}