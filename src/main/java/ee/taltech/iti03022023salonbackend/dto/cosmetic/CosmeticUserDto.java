package ee.taltech.iti03022023salonbackend.dto.cosmetic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CosmeticUserDto {
     private Long userId;
     private Long cosmeticId;
     private String password;

     public CosmeticUserDto(Long userId, Long cosmeticId, String password) {
         this.userId = userId;
         this.cosmeticId = cosmeticId;
         this.password = password;
     }
}
