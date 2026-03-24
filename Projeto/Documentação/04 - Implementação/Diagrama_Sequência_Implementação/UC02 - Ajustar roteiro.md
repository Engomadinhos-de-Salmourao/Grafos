# UC02 - Ajustar Roteiro
---
![](https://www.plantuml.com/plantuml/png/ZLDTJXin47xtAIpnGYH0eWW4VQXOijaA2Qa8w04czf2uU3tBs2PAgvwc3vLwZLoiZfDDGtOHzKhxC_Fzp8ozbpu0XrXR0J6GY_KOMGZ_P5m33BNqKz0qCsuY7y5wtCemHQSLiKEMnn-sAVWSqIcKFuIK1JFD7gP6FJdqNXwdq8rvn1lZy8fO8m_94ikzYxosiw2U9jbZZQWkbbT2t087eqm3BhoIEClhDQ0Y5-x3tA8SazKxv1s7sTG453-tEh76OPWtA8d3b8G05HBkgu_0XkHUUJOggdCnNae0p-KT1JHCm-J9P2roWfnMfwCJeLQX5d6F_68Kffw6rScmsatVOKFU9AjvGXJbLNc-GHJy7CqBVMPux8EX2oPi2f5xe_Eo65LjEBCsGT7sSVYff_AF4hv57u3NgNrUFQ_rmIg3vSENWUWGazfkb-zB3OJOuIILpZ2Iq3Xw1NnBVAt_gGn45thOKqORw3FhdslY93l6IVOiqGVZDkKVwTNtaKaLzFFxoyWW8S06QPooqIz-aTGeBIcmGietHv6ue8qov82l0zRx9jq60gr7oLZJulV_AsN22tPqm0Pas2YbH9DV6kGQtEAFKmRUKLv9i5og7cM1sqp6pErp8TPOTrNxXiUemAfeysc2TYyEv6GvrDI3qztjVUE0hxb5FfoQH1UJcEtVleKF4QpvlcHS9hjOsxy0)
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
control ArquivoGrafoRepository #FADBD8
entity Roteiro #E8DAEF
entity Grafo #E8DAEF

Usuario -> RoteiroController : ajustarRoteiro(roteiroId, ajusteDTO)
RoteiroController -> RoteiroService : ajustarRoteiro(roteiroId, ajuste)

RoteiroService -> RoteiroRepository : buscarPorId(roteiroId)
RoteiroRepository --> RoteiroService : Roteiro

RoteiroService -> ArquivoGrafoRepository : carregarPorDestino(roteiro.destino.id)
ArquivoGrafoRepository --> RoteiroService : Grafo

alt inclusão de local
  RoteiroService -> Roteiro : adicionarItem(item)
else remoção de local
  RoteiroService -> Roteiro : removerItem(item)
else alteração de tempo de permanência
  RoteiroService -> Roteiro : alterar item.tempoPermanencia
else alteração de ordem
  RoteiroService -> RoteiroService : recalcularRota(roteiro, grafo)
end

RoteiroService --> RoteiroController : roteiroAjustado
RoteiroController --> Usuario : roteiroAtualizado
@enduml
```
