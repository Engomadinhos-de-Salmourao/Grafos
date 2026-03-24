# UC11 - Gerenciar Locais e Conexões
---
![](https://www.plantuml.com/plantuml/png/lL9TJjj047xVKwp42v7A0qMWK2aL9ewhIf6A2XUOx4wILTOxONQT45KzJHzw42xMzKyW9a6jfOBpb9bllX_xk_81E1Iv5L04SaK-GHR2BunR0aCk_HmqhOsRoIbOZpiRfi9fHUoGvUd7tHEyBz0fb3-4b0aphU_cHYqSUYzFusXifZWs3g-9DVAGBB4ykgoUtIsenQpIQ13PPVcBY1lWO9HPWWijYelgQGCoSk4sR2pA2LdTthrKMCzDGF5p9uar3iDcYP8up4a8K27Y4fqRPtnWqALiUZ5AihwOb4J06_c5OKh3gCjaBPQ0y-nyT2PKFQhtjyWhep2c6cPdm-nfcV1zOLPKORxZaho9Yfi8IzBhj2_G1HCQ2NaqwgV9ADiEnyKC-EKm-i07UCQAjlcJptjsFqdGHXbom1NZiKOVZAEl-eEqvI2z-zOJxQkAg9Nhhomz8Le7aU1mzaYbW1dZpGrnMjCyq_N4AsSd-vQgltkwzNZVSMEpsXvYK-IcXdDu_FtuYpgayM1NxHSoAp6l1UaiVV2RLc6I9g2c_UyNhzhLY4OBXTv7_i50Ra-vrCQ7A2i7WsvjQgfuJ6ncc9UvTrw1gfTTk_KldF-dQMcDVekwRKCqVj-ySasKT-bTa-cfUT2fULVeT97RFm00)
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

actor Administrador #D6EAF8
boundary GrafoController #D5F5E3
control GrafoService #FCF3CF
control ArquivoGrafoRepository #FADBD8
entity Grafo #E8DAEF
entity Lugar #E8DAEF
entity Conexao #E8DAEF

Administrador -> GrafoController : adicionarLugar(destinoId, lugarDTO)
GrafoController -> GrafoService : adicionarLugar(destinoId, lugar)

GrafoService -> ArquivoGrafoRepository : carregarPorDestino(destinoId)
ArquivoGrafoRepository --> GrafoService : Grafo

GrafoService -> Grafo : adicionarLugar(lugar)
Grafo --> GrafoService : confirmação

GrafoService -> ArquivoGrafoRepository : salvar(destinoId, grafo)
ArquivoGrafoRepository --> GrafoService : confirmação

GrafoService --> GrafoController : lugarAdicionado
GrafoController --> Administrador : sucesso

== Adicionar conexão ==

Administrador -> GrafoController : adicionarConexao(origemId, destinoId, conexaoDTO)
GrafoController -> GrafoService : adicionarConexao(origemId, destinoId, conexao)

GrafoService -> ArquivoGrafoRepository : carregarPorDestino(destinoDoGrafo)
ArquivoGrafoRepository --> GrafoService : Grafo

GrafoService -> Grafo : adicionarConexao(conexao)
Grafo --> GrafoService : confirmação

GrafoService -> ArquivoGrafoRepository : salvar(destinoDoGrafo, grafo)
ArquivoGrafoRepository --> GrafoService : confirmação

GrafoService --> GrafoController : conexaoAdicionada
GrafoController --> Administrador : sucesso
@enduml
```
