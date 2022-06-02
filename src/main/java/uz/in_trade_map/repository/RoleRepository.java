package uz.in_trade_map.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.in_trade_map.entity.Role;
import uz.in_trade_map.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);

//    @Query(value = "select cast(p.id as varchar) as productId, t.to_warehouse_id as warehouseId , p.name as productName,pwa.amount as productAmount from product_with_amount pwa join product p on pwa.product_id = p.id join transfer t on t.id = pwa.transfer_id and t.id=:tranferId",nativeQuery = true)
//    List<PwaInterface> lflfl(@Param(value = "tranferId") UUID tranferId);
}
