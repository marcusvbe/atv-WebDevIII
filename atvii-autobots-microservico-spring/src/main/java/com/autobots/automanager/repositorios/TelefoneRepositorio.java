package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepositorio extends JpaRepository<Telefone, Long> {
}