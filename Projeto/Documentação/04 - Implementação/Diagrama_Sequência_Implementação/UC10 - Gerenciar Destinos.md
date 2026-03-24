# UC10 - Gerenciar Destinos
---
![](https://www.plantuml.com/plantuml/png/bPBDJjj04CVl-nGhSMcaSg0857AeC75SLK9gLNY1oUw4ZBBU2RFZf57Lf-cX3yABrOvTY34vi3b4olpc_w79TL8GhSfWe5AELJb7CIQjAAv1eBHf2Pwt51_i0aB2Wubm5RrZYIZsxDFX2ZvM61tQNyRQJ8ItzqjoguWfsJDZxIqjy9OYth1ub2a75djojN-7Kt2hXxr7HnHNzQSXleCeELf3r9x4vVxrWOAZtkake9rpyFtPQuVjaXJDxuCcWIBgResMHPTi33YjkSoN52cfW6ziyyjPLepDl142sTal0WkUSbJX4B01BegBsRbnxK_j_0vbGmxhLjFYV5ey3pDvh6Z3U-O7hZcHikngBCjlyh71gAITXJsPZVDiLXZJ3tJwUH1XOXruQ039CIb5_k3Rx_p-syZqyKQX7_Bu-iYO7bbl7camiVYJak8hWQz-P8vid0vYj7_1WMLBDIc5G6npEjl9j-XRKf4xntSqI10s82_HFxwelAU5uxWWAU7fxzEVWVsRrqkLmvHuSAY6xXz_yh_vjBkOPtEDqLTb-0S0)
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

Administrador -> GrafoController : cadastrarDestino(destinoDTO)
GrafoController -> GrafoService : cadastrarDestino(destino)

GrafoService -> ArquivoGrafoRepository : existeDestino(destinoId)
ArquivoGrafoRepository --> GrafoService : false

GrafoService -> Grafo : criar para destino
Grafo --> GrafoService : novoGrafo

GrafoService -> ArquivoGrafoRepository : salvar(destinoId, novoGrafo)
ArquivoGrafoRepository --> GrafoService : confirmação

GrafoService --> GrafoController : sucesso
GrafoController --> Administrador : destinoCadastrado
@enduml
```
