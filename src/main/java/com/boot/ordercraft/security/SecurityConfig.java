package com.boot.ordercraft.security;

import com.boot.ordercraft.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ---------- Public Authentication Endpoints ----------
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/setup/init").permitAll()
                .requestMatchers("/testmail").permitAll()
               

                
                // ---------- User Management ----------
                .requestMatchers(
                        "/api/users/reset-password",
                        "/api/users/forgot-password",
                        "/api/users/unlock-account",
                        "/api/users/reset-password-with-otp",
                        "/api/users/send-otp",
                        "/api/users/verify-otp",
                        "/api/users/roles",
                        "/api/users/getallusers",
                        "/api/users/deactivate/**",
                        "/api/users/getSupplier",
                        "/api/users/role/**",
                        "/api/customers"
                ).permitAll()

       
                
                // Register only for ADMIN
                .requestMatchers("/api/users/register").hasRole("ADMIN")
                
             // Procurement Officer ONLY
                .requestMatchers("/api/procurement-officer/**")
                    .hasAuthority("PROCUREMENT_OFFICER")

                // Remaining API requires Auth
                .requestMatchers("/api/**").authenticated()
                

                // ---------- General System Public API (Your Full List Merged) ----------
                .requestMatchers(
                        "/api/purchase-orders/**",
                        "/api/payments/orders/**",
                        "/api/procurement/orders/purchase-orders",
                        "/api/products",
                        "/api/suppliers",
                        "/api/orders",
                        "/api/internal-order",
                        "/api/orders/**",
                        "/api/orders/*/cancel",
                        "/api/users/*/orders",
                        "/api/search",
                        "/api/return-orders",
                        "/api/getallorders",
                        "/api/customers",
                        "/api/customers/id/**",
                        "/api/customers/post",
                        "/api/invoices/generate/**",
                        "/api/invoices/pdf/**",
                        "/api/payments/all",
                        "/api/suppliers/**",
                        "/api/raw-material/supplier/**",
                        "/api/raw-materials",
                        "/api/customers/**",
                        "/api/reports/purchase-order/**"
                ).permitAll()

                // ---------- Supplier Report GET ----------
                .requestMatchers(HttpMethod.GET, "/api/suppliers/report/**").permitAll()

                // ---------- Production & Inventory Public Endpoints ----------
                .requestMatchers(
                        "/api/production/create",
                        "/api/production/{scheduleId}/complete",
                        "/api/raw-materials/supplier/{id}",
                        "/api/raw-material/{id}/suppliers",
                        "/api/production/auto-schedule",
                        "/api/production/low-stock",
                        "/api/production/low-stock-alert",
                        "/api/production/dispatch/{purchaseOrderId}",
                        "/api/production/complete-all",
                        "/api/products/stocks",
                        "/api/production/**",
                        "/api/dashboard",
                        "/api/low-stock-alerts/**",
                        "/api/production-tracking/**",
                        "/api/products/**",
                        "/api/inventory/products/**",
                        "/api/categories/**",
                        "/api/product/**",
                        "/api/reports/export/**",
                        "/api/locked-accounts/**",
                        "/api/return-orders/fetch",
                        "/api/procurement-dashboard/**",
                        "/api/supplier-ratings/**",
                        "/api/payments/**",
                        "/api/purchase-orders/**",
                        "/api/production-manager/**"
                ).permitAll()

                // ---------- Protected Reports ----------
                .requestMatchers(
                        "/api/reports/production-schedule/pdf",
                        "/api/reports/production-schedule/csv"
                ).hasAnyRole("PRODUCTION_MANAGER", "INVENTORY_MANAGER", "PROCUREMENT_OFFICER", "ADMIN")

                // Inventory report restricted
                .requestMatchers("/api/reports/inventory/**")
                    .hasAnyRole("INVENTORY_MANAGER", "ADMIN")

                // ---------- Swagger / Actuator ----------
                .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()

                // ---------- Remaining API requires Auth ----------
                .requestMatchers("/api/**").authenticated()
                
                


                // Everything else
                .anyRequest().permitAll()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
