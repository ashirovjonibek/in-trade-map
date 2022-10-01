package uz.in_trade_map.utils.request_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.in_trade_map.entity.Statistic;
import uz.in_trade_map.utils.validator.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticRequest {

    @NotNull
    private String titleUz;

    @NotNull
    private String titleRu;

    @NotNull
    private String titleEn;

    @NotNull
    private String titleUzCry;

    @NotNull
    private String textUz;

    @NotNull
    private String textRu;

    @NotNull
    private String textEn;

    @NotNull
    private String textUzCry;

    @NotNull
    private short percent;

    @NotNull
    private String additional;

    public static Statistic convertToStatistic(StatisticRequest request, Statistic statistic) {
        statistic.setTextEn(request.getTextEn());
        statistic.setTextUz(request.getTextUz());
        statistic.setTextRu(request.getTextRu());
        statistic.setTextUzCry(request.getTextUzCry());
        statistic.setTitleEn(request.getTitleEn());
        statistic.setTitleUz(request.getTitleUz());
        statistic.setTitleRu(request.getTitleRu());
        statistic.setTitleUzCry(request.getTitleUzCry());
        statistic.setPercent(request.getPercent());
        statistic.setAdditional(request.getAdditional());
        return statistic;
    }
}
