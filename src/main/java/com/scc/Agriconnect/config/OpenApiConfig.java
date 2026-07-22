package com.scc.Agriconnect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agriConnectOpenAPI() {
        final String schemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("AgriConnect API")
                        .version("v1")
                        .description("""
                                ## AgriConnect Backend API

                                AgriConnect is a cooperative management platform that helps agricultural cooperatives manage their members, products, stock, and sales.

                                ### How to Authenticate
                                1. **Register** a cooperative using `POST /api/auth/register`, or **Login** using `POST /api/auth/login`
                                2. Copy the `accessToken` from the response
                                3. Click the **Authorize** button (🔒) at the top right of this page
                                4. Enter: `Bearer <your_token>` and click **Authorize**
                                5. All secured endpoints will now include your token automatically

                                ### Roles & Access
                                | Role | Description |
                                |------|-------------|
                                | `SYSTEM_ADMIN` | Platform administrator — approves/rejects cooperatives |
                                | `PRESIDENT` | Cooperative president — full access to their cooperative |
                                | `STAFF` | General staff — can manage members, products, and sales |
                                | `ACCOUNTANT` | Manages customer records |
                                | `STOCKMANAGER` | Manages customer records |

                                ### Cooperative Workflow
                                1. A cooperative registers via `/api/auth/register` → status is **PENDING**
                                2. A `SYSTEM_ADMIN` approves the cooperative via `/api/admin/cooperatives/{id}/approve`
                                3. The president can now log in and invite staff via `/api/cooperative/staff/invite`
                                4. Invited staff accept via `/api/auth/accept-invitation` using the emailed token
                                """)
                        .contact(new Contact()
                                .name("AgriConnect Support")
                                .email("support@agriconnect.rw")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components().addSecuritySchemes(schemeName,
                        new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Paste your JWT token here. Obtain it from POST /api/auth/login or POST /api/auth/register")));
    }
}