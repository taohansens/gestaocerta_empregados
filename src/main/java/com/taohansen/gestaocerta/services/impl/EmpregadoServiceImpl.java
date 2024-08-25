package com.taohansen.gestaocerta.services.impl;

import com.taohansen.gestaocerta.entities.Empregado;

import java.util.List;
import java.util.Optional;

public interface EmpregadoServiceImpl {
    List<Empregado> getAll();
    Empregado findById(Long id);
    void deleteById(Long id);
    Empregado insert(Empregado empregado);
    Empregado update(Long id, Empregado empregado);
}
