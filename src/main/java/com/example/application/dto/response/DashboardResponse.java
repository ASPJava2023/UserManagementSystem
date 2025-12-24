package com.example.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private String userName;
    private String quote;
    // Interpreting 'Logout flag' as a boolean indicating if the session is valid or
    // prompt to logout?
    // Or maybe the user meant 'Logout link'?
    // I'll add a boolean 'forceLogout' just in case, or maybe it's irrelevant.
    // Let's assume it's just a boolean field as requested.
    private boolean logoutFlag;
}
