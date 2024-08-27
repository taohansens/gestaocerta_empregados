package com.taohansen.mod.empregados.services.impl;

import com.taohansen.mod.empregados.dtos.EmpregadoDTO;

import java.util.List;

public interface EmpregadoServiceImpl {
    List<EmpregadoDTO> getAll();
    EmpregadoDTO findById(Long id);
    void deleteById(Long id);
    EmpregadoDTO insert(EmpregadoDTO dto);
    EmpregadoDTO update(Long id, EmpregadoDTO dto);
}
