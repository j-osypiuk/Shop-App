package com.example.security;

import com.example.user.Role;
import com.example.user.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class UserAccessDecisionManager implements AuthorizationManager<RequestAuthorizationContext> {

    // Checks if principal (CUSTOMER) is trying to access his own data by comparing request path variable user id with principal id
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext ctx) {
        User principal = (User) authenticationSupplier.get().getPrincipal();

        if (principal.getRole() == Role.ADMIN || principal.getRole() == Role.EMPLOYEE)
            return new AuthorizationDecision(true);

        Long userId = Long.parseLong(ctx.getVariables().get("id"));

        return new AuthorizationDecision(userId.equals(principal.getUserId()));
    }
}
