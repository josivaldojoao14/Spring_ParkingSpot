package com.api.parkingcontrol.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.dtos.UserDto;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.RoleRepository;
import com.api.parkingcontrol.repositories.UserRepository;
import com.api.parkingcontrol.services.exceptions.UserNotFoundException;
import com.api.parkingcontrol.services.interfaces.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
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
        UserModel user = userRepository.findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public List<UserDto> findAll() {
        List<UserModel> userModels = userRepository.findAll();
        List<UserDto> listUsersDto = userModels.stream().map(x -> new UserDto(x)).collect(Collectors.toList());
        return listUsersDto;
    }

    @Override
    public UserDto findUserById(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found in the database"));
        return new UserDto(user);
    }

    @Override
    public UserDto createUser(UserDto user) {
        UserModel userModel = new UserModel();
        userModel.setFullName(user.getFullName());
        userModel.setUsername(user.getUsername());
        userModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userModel.setPhone(user.getPhone());

        UserModel newUser = userRepository.save(userModel);
        return new UserDto(newUser);
    }

    @Override
    public UserDto updateUser(UserDto user, UUID id) {
        UserDto userDto = findUserById(id);
        userDto.setFullName(user.getFullName());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setPhone(user.getPhone());

        UserModel updatedUser = userRepository.save(new UserModel(userDto));
        return new UserDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto findByUsername(String username) {
        UserModel userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found in the database"));
        return new UserDto(userModel);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Optional<UserModel> userModel = userRepository.findByUsername(username);
        RoleModel role = roleRepository.findByName(roleName);
        userModel.get().getRoles().add(role);
        userRepository.save(userModel.get());
    }
}
