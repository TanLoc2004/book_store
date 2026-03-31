package Nhom.VoNguyenTanLoc_2280601829.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Nhom.VoNguyenTanLoc_2280601829.entities.Category;
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    
}