resource "keycloak_realm" "realm" {
  realm             = "example"
  enabled           = true
  display_name      = "example realm"
  display_name_html = "<b>example realm</b>"

  login_theme = "base"

  access_code_lifespan = "1h"

  ssl_required = "external"
  #password_policy = "upperCase(1) and length(8) and forceExpiredPasswordChange(365) and notUsername"
  attributes = {
    mycustomAttribute = "myCustomValue"
  }

  smtp_server {
    host = "mailhog"
    port = 1025
    from = "example@example.com"
  }

  internationalization {
    supported_locales = [
      "en",
      "de",
      "es"
    ]
    default_locale = "en"
  }

  security_defenses {
    headers {
      x_frame_options                     = "DENY"
      content_security_policy             = "frame-src 'self'; frame-ancestors 'self'; object-src 'none';"
      content_security_policy_report_only = ""
      x_content_type_options              = "nosniff"
      x_robots_tag                        = "none"
      x_xss_protection                    = "1; mode=block"
      strict_transport_security           = "max-age=31536000; includeSubDomains"
    }
    brute_force_detection {
      permanent_lockout                = false
      max_login_failures               = 30
      wait_increment_seconds           = 60
      quick_login_check_milli_seconds  = 1000
      minimum_quick_login_wait_seconds = 60
      max_failure_wait_seconds         = 900
      failure_reset_time_seconds       = 43200
    }
  }

  web_authn_policy {
    relying_party_entity_name = "Example"
    relying_party_id          = "keycloak.example.com"
    signature_algorithms      = ["ES256", "RS256"]
  }
}

resource "keycloak_openid_client" "openid_client" {
  realm_id      = keycloak_realm.realm.id
  client_id     = "spring-boot"
  client_secret = "aweLEwtZ5H5rLVOQPQx2abuFocHtEv0v"

  name    = "client"
  enabled = true

  standard_flow_enabled        = true
  direct_access_grants_enabled = true
  access_type                  = "CONFIDENTIAL"
  valid_redirect_uris = [
    "http://localhost:8080/*"
  ]
}

resource "keycloak_role" "realm_role" {
  realm_id    = keycloak_realm.realm.id
  name        = "NICE"
  description = "NICE"
}

resource "keycloak_user" "user_joe" {
  realm_id = keycloak_realm.realm.id
  username = "joe"
  enabled  = true

  email          = "joe@example.de"
  email_verified = true
  first_name     = "joe"
  last_name      = "joe"

  initial_password {
    value     = "joe"
    temporary = false
  }
}

resource "keycloak_user" "user_mandy" {
  realm_id = keycloak_realm.realm.id
  username = "mandy"
  enabled  = true

  email          = "mandy@example.de"
  email_verified = true
  first_name     = "mandy"
  last_name      = "mandy"

  initial_password {
    value     = "mandy"
    temporary = false
  }
}

resource "keycloak_user_roles" "user_roles" {
  realm_id = keycloak_realm.realm.id
  user_id  = keycloak_user.user_mandy.id

  role_ids = [
    keycloak_role.realm_role.id,
  ]
}