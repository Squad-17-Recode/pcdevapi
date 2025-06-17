package com.squad17.pcdevapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.repository.EmpresaRepository;

@Service
public class EmpresaDetailsServiceImpl implements UserDetailsService {
    public final EmpresaRepository empresaRepository;

    public EmpresaDetailsServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return (UserDetails) empresaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Empresa não encontrada com username: " + username));
    }
}
