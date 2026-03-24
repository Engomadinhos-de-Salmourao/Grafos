# UC08 - Atualizar Prefêrencias
---
![](https://www.plantuml.com/plantuml/png/bPB1Rjim38RlUWgXkIp0UkYA5caEGzCu1WeKMB5s3y38T4D45ZrAMk0DUveT1kmryc9pqsYncmHOvHlvyV_voxhs0IJ4oYg8WLsivYXA-IMv6WGgxHTGy8hSaox1UknqXACh38j3qMSVkYFuDQ8pg7yehISYl7fSa5awz5wVjQKxAl6E7Dwm52XJjYnwSBKvtIwOvTF6OqlaLyzVIzo3139KWmizYSlDwGCvk_0G6ejwphRezruxh1OKKFti9B7aC3GrQfQmOAN0X9RxuYC8iHvabxD9FbBpPmcG9ZMchQUmjIWjSf5VpCwLUIabuW7b6nbiCqtpysd-klqPQ_OKM9gMc6GtsKYX2nGQVR8bJZJuH6TS0Jazc8sooInNAgrt-l70GcCD8OAbxo3tWYKAEaFWtyKNyhPuh-jEFNlyD5JxAabwb-IDkaEb-W99ifz-hEVHcrQIvRROQGtLFdfwOATKE6wMBk_O-lsT-pC77GsxagI2zU_rB_xFa3ll5F5mmBVR7Na2DKf9Th9rBLZjGTrHFTx-klMVpJtycmElhj4LiR9_0G00)
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
entity "Usuario" as UsuarioDomain #E8DAEF

Usuario -> UsuarioController : atualizarPreferencias(usuarioId, preferenciasDTO)
UsuarioController -> UsuarioService : atualizarPreferencias(usuarioId, preferencias)

UsuarioService -> UsuarioRepository : buscarPorId(usuarioId)
UsuarioRepository --> UsuarioService : Usuario

UsuarioService -> UsuarioDomain : atualizarPreferencias(preferencias)
UsuarioDomain --> UsuarioService : confirmação

UsuarioService -> UsuarioRepository : atualizar(usuario)
UsuarioRepository --> UsuarioService : confirmação

UsuarioService --> UsuarioController : perfilAtualizado
UsuarioController --> Usuario : preferênciasAtualizadas
@enduml
```
