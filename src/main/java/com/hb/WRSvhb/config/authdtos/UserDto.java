package com.hb.WRSvhb.config.authdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long empId; // Use the appropriate identifier
    private String name; // You might need firstName and lastName fields
    private String login; // Email
    private String token;

    // Additional fields can be added as needed
}
