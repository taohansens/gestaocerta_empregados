package com.gestaocerta.microempregados.mappers;

import com.gestaocerta.microempregados.dtos.DocumentoDTO;
import com.gestaocerta.microempregados.dtos.EmpregadoDTO;
import com.gestaocerta.microempregados.dtos.EnderecoDTO;
import com.gestaocerta.microempregados.entities.Documento;
import com.gestaocerta.microempregados.entities.Empregado;
import com.gestaocerta.microempregados.entities.Endereco;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class EmpregadoMapper {

    public EmpregadoDTO toDto(Empregado empregado) {
        if (empregado == null) {
            return null;
        }
        EmpregadoDTO dto = new EmpregadoDTO();
        dto.setId(empregado.getId());
        dto.setNome(empregado.getNome());
        dto.setCpf(empregado.getCpf());
        dto.setDocumento(toDocumentoDto(empregado.getDocumento()));
        dto.setEndereco(toEnderecoDto(empregado.getEndereco()));
        dto.setSexo(empregado.getSexo());
        dto.setNascimento(empregado.getNascimento());
        dto.setJornadaSemanal(empregado.getJornadaSemanal() != null
                ? (int) empregado.getJornadaSemanal().toHours() : 0);
        dto.setJornadaDiaria(empregado.getJornadaDiaria() != null
                ? (int) empregado.getJornadaDiaria().toHours() : 0);
        return dto;
    }

    public Empregado toEntity(EmpregadoDTO dto) {
        if (dto == null) {
            return null;
        }
        Empregado empregado = new Empregado();
        empregado.setId(dto.getId());
        empregado.setNome(dto.getNome());
        empregado.setCpf(dto.getCpf());
        empregado.setDocumento(toDocumentoEntity(dto.getDocumento()));
        empregado.setEndereco(toEnderecoEntity(dto.getEndereco()));
        empregado.setSexo(dto.getSexo());
        empregado.setNascimento(dto.getNascimento());
        empregado.setJornadaSemanal(dto.getJornadaSemanal() != null ?
                Duration.ofHours(dto.getJornadaSemanal()) : null);
        empregado.setJornadaDiaria(dto.getJornadaDiaria() != null ?
                Duration.ofHours(dto.getJornadaDiaria()) : null);
        return empregado;
    }

    private DocumentoDTO toDocumentoDto(Documento documento) {
        if (documento == null) {
            return null;
        }
        DocumentoDTO dto = new DocumentoDTO();
        dto.setTipo(documento.getTipo());
        dto.setNumero(documento.getNumero());
        dto.setOrgaoEmissor(documento.getOrgaoEmissor());
        dto.setDataEmissao(documento.getDataEmissao());
        return dto;
    }

    private Documento toDocumentoEntity(DocumentoDTO dto) {
        if (dto == null) {
            return null;
        }
        Documento documento = new Documento();
        documento.setTipo(dto.getTipo());
        documento.setNumero(dto.getNumero());
        documento.setOrgaoEmissor(dto.getOrgaoEmissor());
        documento.setDataEmissao(dto.getDataEmissao());
        return documento;
    }

    private EnderecoDTO toEnderecoDto(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro(endereco.getLogradouro());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        return dto;
    }

    private Endereco toEnderecoEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        return endereco;
    }

    public void updateEntityFromDto(EmpregadoDTO dto, Empregado entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getNome() != null) {
            entity.setNome(dto.getNome());
        }
        if (dto.getCpf() != null) {
            entity.setCpf(dto.getCpf());
        }
        if (dto.getDocumento() != null) {
            if (entity.getDocumento() == null) {
                entity.setDocumento(new Documento());
            }
            updateDocumentoFromDto(dto.getDocumento(), entity.getDocumento());
        }
        if (dto.getEndereco() != null) {
            if (entity.getEndereco() == null) {
                entity.setEndereco(new Endereco());
            }
            updateEnderecoFromDto(dto.getEndereco(), entity.getEndereco());
        }
        if (dto.getSexo() != null) {
            entity.setSexo(dto.getSexo());
        }
        if (dto.getNascimento() != null) {
            entity.setNascimento(dto.getNascimento());
        }
    }

    private void updateDocumentoFromDto(DocumentoDTO dto, Documento entity) {
        if (dto.getTipo() != null) {
            entity.setTipo(dto.getTipo());
        }
        if (dto.getNumero() != null) {
            entity.setNumero(dto.getNumero());
        }
        if (dto.getOrgaoEmissor() != null) {
            entity.setOrgaoEmissor(dto.getOrgaoEmissor());
        }
        if (dto.getDataEmissao() != null) {
            entity.setDataEmissao(dto.getDataEmissao());
        }
    }

    private void updateEnderecoFromDto(EnderecoDTO dto, Endereco entity) {
        if (dto.getLogradouro() != null) {
            entity.setLogradouro(dto.getLogradouro());
        }
        if (dto.getComplemento() != null) {
            entity.setComplemento(dto.getComplemento());
        }
        if (dto.getBairro() != null) {
            entity.setBairro(dto.getBairro());
        }
        if (dto.getCidade() != null) {
            entity.setCidade(dto.getCidade());
        }
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
        if (dto.getCep() != null) {
            entity.setCep(dto.getCep());
        }
    }
}
