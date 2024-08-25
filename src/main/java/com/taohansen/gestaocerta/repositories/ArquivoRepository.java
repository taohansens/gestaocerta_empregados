package com.taohansen.gestaocerta.repositories;

import com.taohansen.gestaocerta.entities.Arquivo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArquivoRepository extends MongoRepository<Arquivo, String> {
    List<Arquivo> findByEmpregadoId(Long empregadoId);
}
