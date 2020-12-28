
package com.youandi.backend.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.*;


@Data
public class ApiToken {

    private String username;

    private String sid;
    
    private boolean existSession = false;

    private Date created;

    public boolean hasExpired() {
        if(created == null){
            return true;
        }
        LocalDateTime localDateTime = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusHours(1);
        return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).before(new Date());
    }
    
    public ApiToken(LgAccountUser lgAccountUser) {
    	this.username = lgAccountUser.getUserid();
    	
    }
    
    public ApiToken() {}
    

}
