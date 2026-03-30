package com.exemplo.controller;

import com.exemplo.model.AuthenticationRequest;
import com.exemplo.model.AuthenticationResponse;
import com.exemplo.utils.JwtUtil;
import com.exemplo.utils.LdapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

@RestController
public class AuthenticationController {

    @Autowired
    private LdapUtil ldapUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${ldap.userSearchBase}")
    private String userSearchBase;

    @Value("${ldap.userSearchFilter}")
    private String userSearchFilter;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    private void authenticate(String username, String password) throws NamingException {
        DirContext context = ldapUtil.getContext();
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = userSearchFilter.replace("{0}", username);
        SearchResult searchResult = (SearchResult) context.search(userSearchBase, searchFilter, searchControls).next();
        String userDn = searchResult.getNameInNamespace();

        // Now, try to authenticate with the user's credentials
        Hashtable<String, String> authEnv = new Hashtable<>();
        authEnv.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        authEnv.put(javax.naming.Context.PROVIDER_URL, ldapUtil.getContext().getEnvironment().get(javax.naming.Context.PROVIDER_URL).toString());
        authEnv.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
        authEnv.put(javax.naming.Context.SECURITY_PRINCIPAL, userDn);
        authEnv.put(javax.naming.Context.SECURITY_CREDENTIALS, password);

        new javax.naming.directory.InitialDirContext(authEnv);
    }
}
