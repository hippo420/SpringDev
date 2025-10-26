package app.springdev.cache.spring;

import app.springdev.excel.cache.entity.StockStatus;

import java.util.HashMap;
import java.util.Map;

public class SpringCache {
    private final Map<String, StockStatus> database = new HashMap<>();
}
