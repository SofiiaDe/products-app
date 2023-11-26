package com.test.products.model.dto;

import com.test.products.model.entity.Token;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @Size(max = 50, message = "Username length must not exceed 50 symbols")
    @NotBlank(message = "Username must not be null or empty")
    private String username;

    @JsonIgnore
    private List<Token> tokens;
}
