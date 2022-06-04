package uz.in_trade_map.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private Long totalElement;

    private Integer totalPage;

    private Integer page;

    private Integer size;
}
