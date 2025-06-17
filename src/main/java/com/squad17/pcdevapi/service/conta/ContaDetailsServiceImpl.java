package com.squad17.pcdevapi.service.conta;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.squad17.pcdevapi.models.conta.Conta;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;

public abstract class ContaDetailsServiceImpl implements UserDetailsService {

    protected final CandidatoRepository candidatoRepository;
    protected final EmpresaRepository empresaRepository;

    public ContaDetailsServiceImpl(CandidatoRepository candidatoRepository, EmpresaRepository empresaRepository) {
        this.candidatoRepository = candidatoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Conta user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return (UserDetails) user;
    }

    protected abstract Conta findByUsername(String username);
}
