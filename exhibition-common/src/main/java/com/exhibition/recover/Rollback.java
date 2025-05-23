package com.exhibition.recover;

import lombok.Data;

import java.util.List;

@Data
public class Rollback {

    private final String entityName;
    private final List<String> attrNames;
    private final List<String> orgValues;
    private List<String> tempValues;

    public Rollback(String entityName, List<String> attrNames, List<String> orgValues, List<String> tempValues) {
        this.entityName = entityName;
        this.attrNames = attrNames;
        this.orgValues = orgValues;
        this.tempValues = tempValues;
    }

    public Rollback(String entityName, List<String> attrNames, List<String> orgValues) {
        this.entityName = entityName;
        this.attrNames = attrNames;
        this.orgValues = orgValues;
    }
}
