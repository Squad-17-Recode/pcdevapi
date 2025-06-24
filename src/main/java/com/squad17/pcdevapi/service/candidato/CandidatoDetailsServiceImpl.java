package com.squad17.pcdevapi.service.candidato;

import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import com.squad17.pcdevapi.service.conta.ContaDetailsServiceImpl;

@Service("candidatoDetailsService")
public class CandidatoDetailsServiceImpl extends ContaDetailsServiceImpl {

    public CandidatoDetailsServiceImpl(CandidatoRepository candidatoRepository, EmpresaRepository empresaRepository) {
        super(candidatoRepository, empresaRepository);
    }

    @Override
    protected Conta findByUsername(String username) {
        return candidatoRepository.findByUsername(username).orElse(null);
    }
}
