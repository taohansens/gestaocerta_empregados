package com.gestaocerta.microempregados.services.impl;

import com.gestaocerta.microempregados.dtos.EmpregadoDTO;

import java.util.List;

public interface EmpregadoServiceImpl {
    List<EmpregadoDTO> getAll();
    EmpregadoDTO findById(Long id);
    void deleteById(Long id);
    EmpregadoDTO insert(EmpregadoDTO dto);
    EmpregadoDTO update(Long id, EmpregadoDTO dto);
}
