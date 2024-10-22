package org.micro.converters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class KeycloakResourceRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Value("${keycloak.client-ids}")
    private List<String> clientIds;

    /**
     * Convert keycloak JWT resource roles to spring security granted authorities for defined clients
     * @param jwt the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return granted authorities
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String clientId : clientIds) {
            if (resourceAccess.containsKey(clientId)) {
                Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get(clientId);
                List<String> roles = (List<String>) clientResource.get("roles");

                if (roles != null) {
                    authorities.addAll(
                            roles.stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                    .collect(Collectors.toList())
                    );
                }
            }
        }

        return authorities;
    }
}
