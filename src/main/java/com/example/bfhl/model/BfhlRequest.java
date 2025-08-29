package com.example.bfhl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BfhlRequest
{
    @JsonProperty("data")
    @NotNull
    private List<String> data;

    public List<String> getData()
    {
        return data;
    }

    public void setData(List<String> data)
    {
        this.data = data;
    }
}