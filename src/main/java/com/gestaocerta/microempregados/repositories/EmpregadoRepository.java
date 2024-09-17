package com.gestaocerta.microempregados.repositories;

import com.gestaocerta.microempregados.entities.Empregado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpregadoRepository extends JpaRepository<Empregado, Long> {
}
