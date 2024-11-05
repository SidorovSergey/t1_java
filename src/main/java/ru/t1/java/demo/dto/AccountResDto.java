package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResDto extends AccountDto {

    private Long id;

    @Override
    public String toString() {
        return "AccountResDto [id=" + id + ", " + baseString() + "]";
    }
}
