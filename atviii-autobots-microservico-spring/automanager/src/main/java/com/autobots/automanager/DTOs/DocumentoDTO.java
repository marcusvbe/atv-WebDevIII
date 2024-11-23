// src/main/java/com/autobots/automanager/dtos/DocumentoDTO.java
package com.autobots.automanager.DTOs;

import com.autobots.automanager.enumeracoes.TipoDocumento;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
public class DocumentoDTO extends RepresentationModel<DocumentoDTO> {
    private Long id;
    private TipoDocumento tipo;
    private Date dataEmissao;
    private String numero;
}
