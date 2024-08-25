package com.taohansen.gestaocerta.repositories;

import com.taohansen.gestaocerta.entities.Empregado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpregadoRepository extends JpaRepository<Empregado, Long> {
}
