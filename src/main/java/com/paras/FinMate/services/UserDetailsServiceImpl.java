package com.paras.FinMate.services;

import com.paras.FinMate.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername (String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }

}
