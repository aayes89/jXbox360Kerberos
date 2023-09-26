package com.mycompany.xbox360s;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author Slam
 */
public class CustomKerberosLoginModule implements LoginModule {

        private Subject subject;
        private CallbackHandler callbackHandler;
        private Map<String, ?> sharedState;
        private Map<String, ?> options;

        private boolean succeeded = false;
        private Principal userPrincipal;

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
            this.subject = subject;
            this.callbackHandler = callbackHandler;
            this.sharedState = sharedState;
            this.options = options;
        }

        @Override
        public boolean login() throws LoginException {
            // Realiza la lógica de autenticación de Kerberos aquí
            // En este ejemplo, simulamos la autenticación exitosa si el nombre de usuario y la contraseña son "admin"

            Callback[] callbacks = new Callback[2];
            callbacks[0] = new NameCallback("Username");
            callbacks[1] = new PasswordCallback("Password", false);

            try {
                callbackHandler.handle(callbacks);
                String username = ((NameCallback) callbacks[0]).getName();
                char[] password = ((PasswordCallback) callbacks[1]).getPassword();

                // Acepta cualquier usuario y contraseña
                succeeded = true;
                userPrincipal = new Principal() {
                    @Override
                    public String getName() {
                        return username;
                    }
                };
                return true;
                // Verifica las credenciales (en este ejemplo, es un usuario y contraseña fijos)
                /*if ("admin".equals(username) && "admin".equals(new String(password))) {
                    succeeded = true;
                    userPrincipal = new Principal() {
                        @Override
                        public String getName() {
                            return username;
                        }
                    };
                    return true;
                } else {
                    throw new FailedLoginException("Autenticación fallida");
                }*/
            } catch (IOException | UnsupportedCallbackException e) {
                throw new LoginException(e.getMessage());
            }
        }

        @Override
        public boolean commit() throws LoginException {
            if (succeeded) {
                subject.getPrincipals().add(userPrincipal);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean abort() throws LoginException {
            if (succeeded) {
                logout();
            }
            return true;
        }

        @Override
        public boolean logout() throws LoginException {
            subject.getPrincipals().remove(userPrincipal);
            succeeded = false;
            userPrincipal = null;
            return true;
        }
    }
