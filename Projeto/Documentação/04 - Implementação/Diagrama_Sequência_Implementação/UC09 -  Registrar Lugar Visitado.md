# UC09 -  Registrar Lugar Visitado
---
![](https://www.plantuml.com/plantuml/png/dPBFRXen4CRlVWehN5AfEQHH8j935SAoLIKEKTBsFdW7iF1woDWEebMVfeS-I5wiPi78sRfIbEM2PdxpVVF7Dyu3-z0Q0S6J3UqCMGYtqdOD3ArqIsXee-r2pi4up39CmJQAs2BBymzv2Ju6j0hbJo7bY9asNvTQhImw9yzZQAhdEDKMRuaRv34POZcuxhuy2sgrw3mEH7qTVplY3jXhfTTW_Gk9g-vx2THa_OFV6fGpCaslkEUmMMgFubSsYT4M_NQDajWlIGXGFdBVN03M90VLrMHK3yLi9m6yJObnz6Go1ZaYb_Nbv4AeVIWH3yXFMc6SQLnVZEj--XxNv7IqsaPYLDrMmoCnuiUWd-Wpmxo4eVNQR-N9GUX4WakY5RMWhHnCXjLeKYUo4-e7fs41V0oADE_PfyA47oNZGZlFm5DIOBxhs18qT1hsw9VclJGxlTsV1ftNDiRUYN-KClcqdhTgYvvEKiut6yLdmIdWEuhbIJ7hB4FF2gsbGCciVAHO4-tYH9rdjM_tD6kxN5Psxw2ITvSu3XSlcIRSBohQT4HPjSCBghqtbDsbgqXtsNka3IQwwAJ8pZMtyFpd-JUzzc3W0nZz0pYTgt2iDzdzvsMxeD0vAhpLh29o9d-SzuVLD2Hkq3QXDNy1)
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
control ArquivoGrafoRepository #FADBD8
entity "Usuario" as UsuarioDomain #E8DAEF
entity Grafo #E8DAEF
entity Lugar #E8DAEF

Usuario -> UsuarioController : registrarLocalVisitado(usuarioId, lugarId, destinoId)
UsuarioController -> UsuarioService : registrarLocalVisitado(usuarioId, lugarId, destinoId)

UsuarioService -> UsuarioRepository : buscarPorId(usuarioId)
UsuarioRepository --> UsuarioService : Usuario

UsuarioService -> ArquivoGrafoRepository : carregarPorDestino(destinoId)
ArquivoGrafoRepository --> UsuarioService : Grafo

UsuarioService -> Grafo : buscarLugarPorId(lugarId)
Grafo --> UsuarioService : Lugar

UsuarioService -> UsuarioDomain : registrarLugarVisitado(lugar)
UsuarioDomain --> UsuarioService : confirmação

UsuarioService -> UsuarioRepository : atualizar(usuario)
UsuarioRepository --> UsuarioService : confirmação

UsuarioService --> UsuarioController : sucesso
UsuarioController --> Usuario : localVisitadoRegistrado
@enduml
```
