package Nhom.VoNguyenTanLoc_2280601829.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Nhom.VoNguyenTanLoc_2280601829.entities.Role;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{
Role findRoleById(Long id);
}
