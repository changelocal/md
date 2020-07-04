package com.arc.db.jsd.result;

import lombok.Getter;

import java.util.List;


public class BuildResult {
    @Getter
    private String sql;
    @Getter
    private List<Object> args;

    public BuildResult(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("sql: %s, args: %s", sql, args == null ? "null" : args.toString());
    }
}
