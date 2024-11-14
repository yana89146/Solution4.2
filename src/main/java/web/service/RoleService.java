package web.service;

import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public interface RoleService {

    List<Role> getAllRoles();

}
