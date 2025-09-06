package com.apiinabox.account.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private String id;
    private String username;
    private String email;
    private String fullName;
    private String createdAt;
    private String passwordHash;

}



