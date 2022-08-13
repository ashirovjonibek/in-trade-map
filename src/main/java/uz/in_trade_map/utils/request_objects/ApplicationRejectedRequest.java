package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRejectedRequest {
    @NotNull
    private Integer applicationId;
    @NotNull(minLength = 15,maxLength = 200)
    private String rejectMessage;
}
