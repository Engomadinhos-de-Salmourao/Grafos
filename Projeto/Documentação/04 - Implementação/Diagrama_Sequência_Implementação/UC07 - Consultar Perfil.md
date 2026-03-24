# UC07 - Consultar Perfil
---
![](https://www.plantuml.com/plantuml/png/VO_1Ri8m38RlUOf8NBO31uP0m041XKf87D3O7Y1D3Or8uyv9XgffxxvILgcaRCdD_dv_zi8wGSxdcWdlqFWy0MBCdfKf18cSsqoaU57cn8z2MsXr2Br99P81ueENTWG-F1W9_8jnlYJ2orkcvDc0jNnGbNRg23jbO8MK0aMeaNXlMhzsLyZpgNRy4l6q-bTYByWfgGfXtDs8SVtkWHYDExXI0qzGfq4uC5movO1zjoxHoe0h2-18BaF6X7GLzswz88Myjnvlbl649TSHWigc4Q5nX5e3LSWe7cs6JDvA3N40-bGIgfkYU1Z5OViL2hJA8PKLiLolrXF66cb__a0punADzTe9sWCTbNxoDsQRFhCktfy7Y_m_W0Lm7GyNdV74Mrd5aRPfe0_G_WD_K-ZA_ZYugDTiKbsYbUlG2p2fp_KF)
---
```plantuml
@startuml
autonumber

skinparam shadowing false
skinparam roundcorner 12
skinparam sequence {
  ArrowThickness 1
  LifeLineBorderColor #999999
  LifeLineBackgroundColor #F9F9F9
  ParticipantBorderColor #666666
  ParticipantFontStyle bold
  ParticipantBackgroundColor white
}
skinparam linetype ortho

actor Usuario #D6EAF8
boundary UsuarioController #D5F5E3
control UsuarioService #FCF3CF
control UsuarioRepository #FADBD8

Usuario -> UsuarioController : consultarPerfil(usuarioId)
UsuarioController -> UsuarioService : consultarPerfil(usuarioId)

UsuarioService -> UsuarioRepository : buscarPorId(usuarioId)
UsuarioRepository --> UsuarioService : Usuario

UsuarioService --> UsuarioController : perfilUsuario
UsuarioController --> Usuario : perfilUsuario
@enduml
```
