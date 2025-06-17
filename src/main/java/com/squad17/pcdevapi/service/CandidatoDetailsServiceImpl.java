package com.squad17.pcdevapi.service;

import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.repository.CandidatoRepository;
import com.squad17.pcdevapi.repository.EmpresaRepository;

@Service("candidatoDetailsService")
public class CandidatoDetailsServiceImpl extends UserDetailsServiceImpl {

    public CandidatoDetailsServiceImpl(CandidatoRepository candidatoRepository, EmpresaRepository empresaRepository) {
        super(candidatoRepository, empresaRepository);
    }

    @Override
    protected Conta findByUsername(String username) {
        return candidatoRepository.findByUsername(username).orElse(null);
    }
}
