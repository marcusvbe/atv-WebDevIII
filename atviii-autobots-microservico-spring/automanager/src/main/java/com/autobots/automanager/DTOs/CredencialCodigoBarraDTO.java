// src/main/java/com/autobots/automanager/dtos/CredencialCodigoBarraDTO.java
package com.autobots.automanager.DTOs;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredencialCodigoBarraDTO extends CredencialDTO {
    private long codigo;
}