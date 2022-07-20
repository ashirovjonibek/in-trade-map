package uz.in_trade_map.utils.projections;

public interface MaxMinProjectionForProduct {
    Float getMinPriceUSD();

    Float getMaxPriceUSD();

    Float getMinPriceUZS();

    Float getMaxPriceUZS();

    Float getMinExportPriceUZS();

    Float getMaxExportPriceUZS();

    Float getMinExportPriceUSD();

    Float getMaxExportPriceUSD();
}
