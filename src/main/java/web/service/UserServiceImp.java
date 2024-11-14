package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import web.model.Role;
import web.model.User;
import web.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImp implements  UserDetailsService,UserService{

private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).get();
        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateById(User user,Long id) {
        User updatedUser = userRepository.findById(id).get();
        updatedUser.getRoles().clear();
        updatedUser.setRoles(user.getRoles());

        updatedUser.setUsername(user.getUsername());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setAge(user.getAge());
        userRepository.save(updatedUser);

    }

    @Override
    public User findByUsername(String firstname) {
        return userRepository.findByUsername(firstname);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        User user = optionalUser.get();


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities(user.getRoles()));

    }

    private Set<GrantedAuthority> authorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
                .collect(Collectors.toSet());
    }
}
