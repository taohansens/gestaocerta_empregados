package com.taohansen.gestaocerta.mappers;

import com.taohansen.gestaocerta.dtos.PontoEletronicoMinDTO;
import com.taohansen.gestaocerta.entities.PontoEletronico;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class PontoEletronicoMapper {

    public PontoEletronicoMinDTO toMinDto(PontoEletronico pontoEletronico) {
        if (pontoEletronico == null) {
            return null;
        }
        PontoEletronicoMinDTO dto = new PontoEletronicoMinDTO();
        dto.setData(pontoEletronico.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setHoraEntrada(pontoEletronico.getHoraEntrada() != null
                        ? pontoEletronico.getHoraEntrada().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "-"
        );

        dto.setHoraSaida(pontoEletronico.getHoraSaida() != null
                        ? pontoEletronico.getHoraSaida().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "-"
        );
        return dto;
    }
}
