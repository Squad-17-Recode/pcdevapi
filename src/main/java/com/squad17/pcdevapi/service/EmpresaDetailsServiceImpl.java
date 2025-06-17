package com.squad17.pcdevapi.service;

import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.repository.CandidatoRepository;
import com.squad17.pcdevapi.repository.EmpresaRepository;

@Service("empresaDetailsService")
public class EmpresaDetailsServiceImpl extends UserDetailsServiceImpl {

    public EmpresaDetailsServiceImpl(CandidatoRepository candidatoRepository, EmpresaRepository empresaRepository) {
        super(candidatoRepository, empresaRepository);
    }

    @Override
    protected Conta findByUsername(String username) {
        return empresaRepository.findByUsername(username).orElse(null);
    }
}
