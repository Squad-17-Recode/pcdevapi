package com.squad17.pcdevapi.service.empresa;

import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import com.squad17.pcdevapi.service.conta.ContaDetailsServiceImpl;

@Service("empresaDetailsService")
public class EmpresaDetailsServiceImpl extends ContaDetailsServiceImpl {

    public EmpresaDetailsServiceImpl(CandidatoRepository candidatoRepository, EmpresaRepository empresaRepository) {
        super(candidatoRepository, empresaRepository);
    }

    @Override
    protected Conta findByUsername(String username) {
        return empresaRepository.findByUsername(username).orElse(null);
    }
}
