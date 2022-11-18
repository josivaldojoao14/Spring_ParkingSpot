package com.api.parkingcontrol.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            @Lazy PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findUserById(UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        return user;
    }

    @Transactional
    @Override
    public UserModel saveUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        Optional<UserModel> user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Optional<UserModel> user = userRepository.findByUsername(username);
        RoleModel role = roleRepository.findByRoleName(roleName);
        user.get().getRoles().add(role);
        userRepository.save(user.get());
    }
}
