package com.taohansen.mod.empregados.repositories;

import com.taohansen.mod.empregados.entities.Empregado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpregadoRepository extends JpaRepository<Empregado, Long> {
}
