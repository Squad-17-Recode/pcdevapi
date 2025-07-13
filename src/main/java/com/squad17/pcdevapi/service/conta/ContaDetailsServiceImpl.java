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
        // Verifica o tipo de usuário e retorna o UserDetails correspondente
        if (user instanceof com.squad17.pcdevapi.models.candidato.Candidato) {
            return new com.squad17.pcdevapi.service.security.CandidatoUserDetails((com.squad17.pcdevapi.models.candidato.Candidato) user);
        } else if (user instanceof com.squad17.pcdevapi.models.empresa.Empresa) {
            return new com.squad17.pcdevapi.service.security.EmpresaUserDetails((com.squad17.pcdevapi.models.empresa.Empresa) user);
        } else {
            throw new UsernameNotFoundException("Tipo de usuário desconhecido para username: " + username);
        }
    }

    protected abstract Conta findByUsername(String username);
}
