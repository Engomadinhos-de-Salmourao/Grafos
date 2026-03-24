# UC04 - Vizualizar Roteiros Salvos
---
![](https://www.plantuml.com/plantuml/png/ZL7DRXen4BxxAKRYaaZD8OqI4GvLDYnR8UqXQje7CFR0MXWF6TkWRTLthp5PjB29LEzjlf_lczad7oI7kB52na0kRXR8GlYrSLl9SWE-aPhsngrWAQt77i8KdLR43Xbkl_Ob-1RHAOG_0g1WfltFngYrG-_XDergiyJQE7mcriXJiiGmUiolZqgrNkMCTqRrUFWIuqLoCCfifGjd5W_vdHCgSk4rj1PXGLQVOvS9-yO457zxczZK8xHR1EBGa11IXSJxvQDaGp0g7sP5DHQBWuNa5cfIqavJ8fEro8bmNztFxeGwZexuA_BEA4pxJAkxQNK22twBPaVVMIxf1sx9cvJL9bfHFfTZWIwOq4A6OJGRbyMiwePrN4a-3KNNx-RReD44TYQXrloMN6AGja6VELVsu37NNq2Z3yRHN5-BItLdsAqmWKNqwd-SH5-NV3xRTWB9ZZ4Pl12NH_rLp-SJsSsmMQOCS_FuLBoEnwIvxbfVYypuo36JBnq_lB5-lqT96HlUCQcwdpHaFw7JSMF_0G00)
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
boundary RoteiroController #D5F5E3
control RoteiroService #FCF3CF
control RoteiroRepository #FADBD8

Usuario -> RoteiroController : listarRoteiros(usuarioId)
RoteiroController -> RoteiroService : listarRoteiros(usuarioId)

RoteiroService -> RoteiroRepository : listarPorUsuario(usuarioId)
RoteiroRepository --> RoteiroService : List<Roteiro>

RoteiroService --> RoteiroController : roteiros
RoteiroController --> Usuario : listaDeRoteiros
@enduml
```
