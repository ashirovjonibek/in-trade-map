package uz.in_trade_map.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String prettyName;

    public Permissions(String name) {
        this.name = name;
    }

    public Permissions(String name, String prettyName) {
        this.name = name;
        this.prettyName = prettyName;
    }
}