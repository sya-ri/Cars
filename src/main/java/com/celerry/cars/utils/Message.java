package com.celerry.cars.utils;

import com.celerry.cars.Cars;

public enum Message {

    PREFIX {
        @Override
        public String toString() {
            return Cars.getPlugin().getConfig().getString("options.prefix");
        }
    },
    ;

    private final String path;

    Message() {
        this.path = name().toLowerCase().replace("_", ".");
    }

    @Override
    public String toString() {
        return Cars.getPlugin().getConfig().getString("messages." + path).replace("%prefix%", PREFIX.toString());
    }

}