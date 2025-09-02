
package com.okbasalman.api_gateway.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponseDto {
   private String access_token ;
  private int expires_in ;
  private int  refresh_expires_in ;
  private String refresh_token ;
}
